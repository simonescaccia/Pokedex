package it.simonescaccia.pokedex.interfaces

import it.simonescaccia.pokedex.gson.PokemonGson

interface CallbackMoveList {
    suspend fun onSuccess(moveListGson: List<PokemonGson.MoveObjectGson>?)
    fun onError(error: String)
}