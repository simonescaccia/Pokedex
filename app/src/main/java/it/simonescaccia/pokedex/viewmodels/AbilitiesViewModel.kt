package it.simonescaccia.pokedex.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import it.simonescaccia.pokedex.gson.PokemonGson
import it.simonescaccia.pokedex.interfaces.CallbackAbilityList
import it.simonescaccia.pokedex.interfaces.CallbackAbility
import it.simonescaccia.pokedex.models.Ability
import it.simonescaccia.pokedex.persistence.entities.Pokemon
import it.simonescaccia.pokedex.repositories.VolleySingletonRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AbilitiesViewModel(application: Application): AndroidViewModel(application) {

    private val volleyRepository = VolleySingletonRepository.getInstance(application)
    var abilityList: MutableLiveData<MutableList<Ability>> = MutableLiveData()

    //callbacks result list
    private lateinit var listCallbackAbility: MutableList<Ability>
    private var listSize = 0
    private var count = 0

    fun loadAbilities (pokemon: Pokemon) {

        listCallbackAbility = mutableListOf()
        CoroutineScope(Dispatchers.IO).launch {
            /**
             * get the pokemon abilities
             */
            volleyRepository.getAbilitiesGson(pokemon, callbackAbilityList)
        }
    }

    private val callbackAbilityList = object : CallbackAbilityList {
        override fun onSuccess(abilityListGson: List<PokemonGson.AbilityObjectGson>?) {
            if (abilityListGson == null) {
                abilityList.value = mutableListOf()
                return
            }
            /**
             * for each ability listed on pokemon info get the full Ability Object
             */
            listSize = abilityListGson.size
            for (abilityObjectGson in abilityListGson) {
                volleyRepository.getAbility(
                    abilityObjectGson,
                    callbackAbility)
            }
        }

        override fun onError(error: String) {
            Log.d("POKEMON", error)
        }

    }

    private val callbackAbility = object : CallbackAbility {
        override fun onSuccess(ability: Ability) {
            listCallbackAbility.add(ability)
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
            abilityList.value = listCallbackAbility
        }
    }

}