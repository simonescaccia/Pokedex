package it.simonescaccia.pokedex.persistence.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import it.simonescaccia.pokedex.persistence.dao.PokemonDao
import it.simonescaccia.pokedex.persistence.entities.Pokemon
import it.simonescaccia.pokedex.persistence.entities.Type

@Database (entities = [Type::class, Pokemon::class], version = 1, exportSchema = true)
abstract class PokemonDatabase: RoomDatabase() {

    companion object {
        private var db: PokemonDatabase? = null // Singleton
        fun getInstance(context: Context): PokemonDatabase {
            if (db == null)
            db = Room.databaseBuilder(
                context.applicationContext,
                PokemonDatabase::class.java,
                "pokemon.db"
            )
                .createFromAsset("pokemon_db.db")
                .build()
            return db as PokemonDatabase
        }
    }

    abstract fun pokemonDao(): PokemonDao
}