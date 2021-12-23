package it.simonescaccia.pokedex.interfaces

import it.simonescaccia.pokedex.models.Ability

interface CallbackAbility {
    fun onSuccess(ability: Ability)
    fun onError(error: String)
}