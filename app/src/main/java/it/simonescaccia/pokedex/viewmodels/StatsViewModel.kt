package it.simonescaccia.pokedex.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import it.simonescaccia.pokedex.gson.PokemonGson
import it.simonescaccia.pokedex.interfaces.CallbackStat
import it.simonescaccia.pokedex.interfaces.CallbackStatGeneral
import it.simonescaccia.pokedex.interfaces.CallbackStatsList
import it.simonescaccia.pokedex.models.Stat
import it.simonescaccia.pokedex.models.StatsGeneral
import it.simonescaccia.pokedex.persistence.entities.Pokemon
import it.simonescaccia.pokedex.repositories.VolleySingletonRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StatsViewModel(application: Application): AndroidViewModel(application) {

    private val volleyRepository = VolleySingletonRepository.getInstance(application)
    val statList: MutableLiveData<List<Stat>> = MutableLiveData()
    val statsGeneral: MutableLiveData<StatsGeneral> = MutableLiveData()

    //callbacks result list
    private lateinit var listCallbackStat: MutableList<Stat>
    private var listSize = 0
    private var count = 0

    fun loadStats(pokemon: Pokemon) {
        listCallbackStat = mutableListOf()
        CoroutineScope(Dispatchers.IO).launch {
            /**
             * set the pokemon stats
             */
            volleyRepository.getStatsGson(pokemon, callbackStatsList, callbackStatsGeneral)
        }
    }

    //callbacks
    private val callbackStatsList = object : CallbackStatsList {
        override fun onSuccess(statListGson: List<PokemonGson.StatObjectGson>) {
            /**
             * for each stat listed on pokemon info get the full Stat Object
             */
            listSize = statListGson.size
            for (statObjectGson in statListGson) {
                volleyRepository.getStat(
                    statObjectGson,
                    callbackStat
                )
            }
        }

        override fun onError(error: String) {
            Log.d("POKEMON", error)
        }
    }

    private val callbackStat = object : CallbackStat {
        override fun onSuccess(stat: Stat) {
            listCallbackStat.add(stat)
            count++
            postValue()
        }

        override fun onError(error: String) {
            count++
            Log.d("POKEMON", error)
            postValue()
        }
    }

    /**
     * if all thread call callback method, set the live data
     */
    private fun postValue() {
        if(count == listSize) {
            statList.value = listCallbackStat
        }
    }

    private val callbackStatsGeneral = object : CallbackStatGeneral {
        override fun onSuccess(stats: StatsGeneral) {
            statsGeneral.postValue(stats)
        }

        override fun onError(error: String) {
            Log.d("POKEMON", error)
        }

    }
}