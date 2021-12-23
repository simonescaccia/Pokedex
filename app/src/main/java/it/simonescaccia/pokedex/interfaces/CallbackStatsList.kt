package it.simonescaccia.pokedex.interfaces

import it.simonescaccia.pokedex.gson.PokemonGson

interface CallbackStatsList {
    fun onSuccess(statListGson: List<PokemonGson.StatObjectGson>)
    fun onError(error: String)
}
