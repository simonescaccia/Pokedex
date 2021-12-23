package it.simonescaccia.pokedex.repositories

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import it.simonescaccia.pokedex.gson.AbilityGson
import it.simonescaccia.pokedex.gson.MoveGson
import it.simonescaccia.pokedex.gson.PokemonGson
import it.simonescaccia.pokedex.gson.StatGson
import it.simonescaccia.pokedex.interfaces.*
import it.simonescaccia.pokedex.models.Ability
import it.simonescaccia.pokedex.models.Move
import it.simonescaccia.pokedex.models.Stat
import it.simonescaccia.pokedex.models.StatsGeneral
import it.simonescaccia.pokedex.persistence.entities.Pokemon
import it.simonescaccia.pokedex.persistence.entities.Type
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * unico punto di accesso alle risorse in internet tramite la libreria volley
 *
 * le richieste vengono aggiunte tutte in un'unica RequestQueue
 */
class VolleySingletonRepository private constructor(){

    private lateinit var requestQueue: RequestQueue

    companion object {

        //urls
        private const val pokemonUrl = "https://pokeapi.co/api/v2/pokemon/"

        //singleton
        private var volleyAccess: VolleySingletonRepository? = null // Singleton

        //thread safety
        fun getInstance(context: Context) = synchronized(this){
            if (volleyAccess == null) {
                volleyAccess = VolleySingletonRepository()
                volleyAccess!!.requestQueue = Volley.newRequestQueue(context)
            }
            volleyAccess!!
        }

    }

    fun getAbilitiesGson(pokemon: Pokemon, callback: CallbackAbilityList) {
        CoroutineScope(Dispatchers.IO).launch {
            /**
             * get the pokemon json
             */
            val pokemonJsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                "$pokemonUrl${pokemon.id}",
                null,
                {
                    /**
                     * onResponse
                     *
                     * get the list of pokemon abilities
                     */
                    val abilitiesJA = it.getJSONArray("abilities")
                    val abilityObjectType =
                        object : TypeToken<List<PokemonGson.AbilityObjectGson>>() {}.type
                    val abilityListGson = Gson().fromJson<List<PokemonGson.AbilityObjectGson>>(
                        abilitiesJA.toString(),
                        abilityObjectType
                    )

                    callback.onSuccess(abilityListGson)
                },
                {
                    /**
                     * onError
                     */
                    callback.onError(it.message ?: "Error Volley: get the list of pokemon abilities")
                }
            )
            requestQueue.add(pokemonJsonObjectRequest)
        }
    }

    fun getAbility(
        abilityObjectGson: PokemonGson.AbilityObjectGson,
        callback: CallbackAbility
    ){
        CoroutineScope(Dispatchers.IO).launch {
            val abilityJsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                abilityObjectGson.ability.url,
                null,
                { abilityJson ->
                    /**
                     * onResponse
                     *
                     * get the full ability info
                     */
                    val abilityType = object : TypeToken<AbilityGson>() {}.type
                    val abilityGson =
                        Gson().fromJson<AbilityGson>(abilityJson.toString(), abilityType)

                    /**
                     * fill the Ability entry of listOfAbility with the correct language
                     */
                    val ability = Ability(abilityGson, abilityObjectGson.is_hidden)

                    callback.onSuccess(ability)
                },
                {
                    /**
                     * onError
                     */
                    callback.onError(it.message ?: "Error Volley: get the full ability info")
                }
            )
            requestQueue.add(abilityJsonObjectRequest)
        }
    }

    fun getMovesGson(pokemon: Pokemon, callback: CallbackMoveList) {
        CoroutineScope(Dispatchers.IO).launch {
            /**
             * get the pokemon json
             */
            val pokemonJsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                "$pokemonUrl${pokemon.id}",
                null,
                {
                    /**
                     * onResponse
                     *
                     * get the list of pokemon moves
                     */
                    val movesJA = it.getJSONArray("moves")
                    val movesObjectType =
                        object : TypeToken<List<PokemonGson.MoveObjectGson>>() {}.type
                    val moveListGson = Gson().fromJson<List<PokemonGson.MoveObjectGson>>(
                        movesJA.toString(),
                        movesObjectType
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        callback.onSuccess(moveListGson)
                    }
                },
                {
                    /**
                     * onError
                     */
                    callback.onError(it.message ?: "Error Volley: get the list of pokemon moves")
                }
            )
            requestQueue.add(pokemonJsonObjectRequest)
        }
    }

    fun getMove(
        moveObjectGson: PokemonGson.MoveObjectGson,
        listOfTypes: List<Type>,
        callback: CallbackMove
    ){
        CoroutineScope(Dispatchers.IO).launch {
            val moveJsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                moveObjectGson.move.url,
                null,
                { moveJson ->
                    /**
                     * onResponse
                     *
                     * get the full move info
                     */
                    val moveType = object : TypeToken<MoveGson>() {}.type
                    val moveGson =
                        Gson().fromJson<MoveGson>(moveJson.toString(), moveType)

                    /**
                     * fill the Move entry of listOfMove with the correct language
                     */
                    val move = Move(moveGson, listOfTypes)
                    callback.onSuccess(move)
                },
                {
                    /**
                     * onError
                     */
                    callback.onError(it.message ?: "Error Volley: get the full move info")
                }
            )
            requestQueue.add(moveJsonObjectRequest)
        }
    }

    fun getStatsGson(
        pokemon: Pokemon,
        callback: CallbackStatsList,
        callbackStatGeneral: CallbackStatGeneral
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            /**
             * get the pokemon json
             */
            val pokemonJsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                "$pokemonUrl${pokemon.id}",
                null,
                {
                    /**
                     * onResponse
                     **/

                    /**
                     * get the pokemon general stats
                     */
                    val statsGeneral = StatsGeneral()
                    statsGeneral.height = it.getInt("height").toFloat()/10
                    statsGeneral.weight = it.getInt("weight").toFloat()/10
                    callbackStatGeneral.onSuccess(statsGeneral)
                    /**
                     * get the list of pokemon stats
                     */
                    val statsJA = it.getJSONArray("stats")
                    val statObjectType =
                        object : TypeToken<List<PokemonGson.StatObjectGson>>() {}.type
                    val statListGson = Gson().fromJson<List<PokemonGson.StatObjectGson>>(
                        statsJA.toString(),
                        statObjectType
                    )

                    callback.onSuccess(statListGson)
                },
                {
                    /**
                     * onError
                     */
                    callback.onError(it.message ?: "Error Volley: get the pokemon general stats")
                }
            )
            requestQueue.add(pokemonJsonObjectRequest)
        }
    }

    fun getStat(
        statObjectGson: PokemonGson.StatObjectGson,
        callback: CallbackStat
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val statJsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                statObjectGson.stat.url,
                null,
                { statJson ->
                    /**
                     * onResponse
                     *
                     * get the full stats info
                     */
                    val statType = object : TypeToken<StatGson>() {}.type
                    val statGson =
                        Gson().fromJson<StatGson>(statJson.toString(), statType)

                    /**
                     * fill the Stat entry of listOfStats with the correct language
                     */
                    val stat = Stat(statGson, statObjectGson)

                    callback.onSuccess(stat)
                },
                {
                    /**
                     * onError
                     */
                    callback.onError(it.message ?: "Error Volley: get the full stats info" )
                }
            )
            requestQueue.add(statJsonObjectRequest)
        }
    }

}

