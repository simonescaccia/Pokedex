package it.simonescaccia.pokedex.interfaces

import it.simonescaccia.pokedex.models.StatsGeneral

interface CallbackStatGeneral {
    fun onSuccess(stats: StatsGeneral)
    fun onError(error: String)
}
