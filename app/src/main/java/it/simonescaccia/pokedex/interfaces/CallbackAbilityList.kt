package it.simonescaccia.pokedex.interfaces

import it.simonescaccia.pokedex.gson.PokemonGson

interface CallbackAbilityList {
    fun onSuccess(abilityListGson: List<PokemonGson.AbilityObjectGson>?)
    fun onError(error: String)
}