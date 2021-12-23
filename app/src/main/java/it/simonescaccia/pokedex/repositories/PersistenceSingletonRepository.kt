package it.simonescaccia.pokedex.repositories

import android.content.Context
import it.simonescaccia.pokedex.persistence.databases.PokemonDatabase
import it.simonescaccia.pokedex.persistence.entities.Pokemon
import it.simonescaccia.pokedex.persistence.entities.Type
import kotlinx.coroutines.*
import java.util.*

/**
 * unico punto di accesso alla persistenza
 *
 * fornisce i metodi necessari per interagire con il database senza che il chiamante
 * si preoccupi di creare nuove Coroutine
 *
 * la prima getInstance viene lanciata durante lo splash screen in modo da poter caricare tutti i dati
 * prima di mostrare la main activity
 */
@SuppressWarnings("SpellCheckingInspection")
class PersistenceSingletonRepository private constructor(){

    companion object {

        private var dbAccess: PersistenceSingletonRepository? = null // Singleton
        private lateinit var db: PokemonDatabase
        private var listOfPokemon: MutableList<Pokemon>? = null
        private var listOfType: MutableList<Type>? = null
        private lateinit var resultType: Deferred<Unit>
        private lateinit var resultPokemon: Deferred<Unit>
        private var onSetup = 0

        //thread safety
        fun getInstance(context: Context) = synchronized(this){
            if (dbAccess == null) {
                //first getInstance
                dbAccess = PersistenceSingletonRepository()
                db = PokemonDatabase.getInstance(context)

                resultType = CoroutineScope(Dispatchers.IO).async{
                    //load all type
                    listOfType = db.pokemonDao().loadAllType()
                    return@async
                }
                resultPokemon = CoroutineScope(Dispatchers.IO).async{
                    //load all pokemon
                    val lan = Locale.getDefault().language
                    listOfPokemon = if (lan.equals("it")) {
                        db.pokemonDao().loadAllPokemonIt()
                    } else {
                        db.pokemonDao().loadAllPokemon()
                    }
                    return@async
                }
            }
            dbAccess!!
        }

    }

    suspend fun getListOfPokemon() : MutableList<Pokemon> {
        return if(onSetup < 2) {
            //first run, pokemon should be loaded by the splash screen
            //and used by service and miain activity
            resultPokemon.await()
            onSetup++
            listOfPokemon!!
        } else {
            val lan = Locale.getDefault().language
            listOfPokemon = if (lan.equals("it")) {
                db.pokemonDao().loadAllPokemonIt()
            } else {
                db.pokemonDao().loadAllPokemon()
            }
            listOfPokemon!!
        }
    }

    suspend fun getListOfType() : MutableList<Type> {
        resultType.await()
        return listOfType!!
    }

    fun getListOfFavoritePokemon(): MutableList<Pokemon> {
        val lan = Locale.getDefault().language
        listOfPokemon = if (lan.equals("it")) {
            db.pokemonDao().loadAllFavoritePokemonIt()
        } else {
            db.pokemonDao().loadAllFavoritePokemon()
        }
        return listOfPokemon!!
    }

    fun updatePokemon(pokemon: Pokemon) {
        db.pokemonDao().update(pokemon.id, pokemon.favorite)
    }

}