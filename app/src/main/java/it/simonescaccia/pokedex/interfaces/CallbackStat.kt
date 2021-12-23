package it.simonescaccia.pokedex.interfaces

import it.simonescaccia.pokedex.models.Stat

interface CallbackStat {
    fun onSuccess(stat: Stat)
    fun onError(error: String)
}
