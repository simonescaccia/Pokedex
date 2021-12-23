package it.simonescaccia.pokedex.interfaces

import it.simonescaccia.pokedex.models.Move

interface CallbackMove {
    fun onSuccess(move: Move)
    fun onError(error: String)
}