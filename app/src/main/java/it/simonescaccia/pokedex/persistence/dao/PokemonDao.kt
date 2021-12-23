package it.simonescaccia.pokedex.persistence.dao

import androidx.room.*
import it.simonescaccia.pokedex.persistence.entities.Pokemon
import it.simonescaccia.pokedex.persistence.entities.Type

@Dao
interface PokemonDao {

    /**
     * Non posso aggiornare direttamente con l'etichetta @Update di Room perchè il tipo
     * del Pokémon dipende dalla lingua del dispositivo per comodità nell'applicazione
     */
    @Query("UPDATE Pokemon SET favorite = :favorite WHERE id = :id")
    fun update(id: Int, favorite: Int)

    @Query("SELECT * FROM Type")
    fun loadAll(): MutableList<Type>

    /**
     * i pokemon sono ordinati per numero nel pokedex, trasformazioni dello stesso pokemon
     * hanno stesso numero di pokedex quindi vanno ordinati anche tramite il campo order.
     * Order contiene però valori anche negativi.
     * I pokemon che contengono valori negativi vanno posizionati alla fine dello stesso tipo
     * di pokemon
     */
    @Query("SELECT * FROM Pokemon ORDER BY pokedexnumber, CASE WHEN `order`<0 THEN 1 ELSE 0 END ,`order`")
    fun loadAllPokemon(): MutableList<Pokemon>

    /**
     * lista dei pokemon con informazioni dei pokemon in italiano
     */
    @Query("SELECT id, Pokemon.name as name, Pokemon.color as color, pokedexnumber, `order`, species, linkImage, Type1.it as type1, Type2.it as type2, favorite" +
            " FROM Pokemon" +
            " INNER JOIN Type as Type1 ON Pokemon.type1 = Type1.name" +
            " LEFT JOIN Type as Type2 ON Pokemon.type2 = Type2.name" +
            " ORDER BY pokedexnumber, CASE WHEN `order`<0 THEN 1 ELSE 0 END ,`order`")
    fun loadAllPokemonIt (): MutableList<Pokemon>

    @Query("SELECT * FROM Type")
    fun loadAllType(): MutableList<Type>

    @Query("SELECT * FROM Pokemon WHERE favorite = 1 ORDER BY pokedexnumber, CASE WHEN `order`<0 THEN 1 ELSE 0 END ,`order`")
    fun loadAllFavoritePokemon(): MutableList<Pokemon>

    @Query("SELECT id, Pokemon.name as name, Pokemon.color as color, pokedexnumber, `order`, species, linkImage, Type1.it as type1, Type2.it as type2, favorite" +
            " FROM Pokemon" +
            " INNER JOIN Type as Type1 ON Pokemon.type1 = Type1.name" +
            " LEFT JOIN Type as Type2 ON Pokemon.type2 = Type2.name" +
            " WHERE favorite = 1" +
            " ORDER BY pokedexnumber, CASE WHEN `order`<0 THEN 1 ELSE 0 END ,`order`")
    fun loadAllFavoritePokemonIt (): MutableList<Pokemon>
}