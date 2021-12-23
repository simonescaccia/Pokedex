package it.simonescaccia.pokedex.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import it.simonescaccia.pokedex.persistence.entities.Pokemon
import it.simonescaccia.pokedex.repositories.PersistenceSingletonRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application): AndroidViewModel(application) {

    private var persistenceRepository = PersistenceSingletonRepository.getInstance(application)
    var pokemonList: MutableLiveData<MutableList<Pokemon>> = MutableLiveData()

    fun loadLists() {
        CoroutineScope(Dispatchers.IO).launch {
            pokemonList.postValue(persistenceRepository.getListOfFavoritePokemon())
        }
    }

    fun update(pokemon: Pokemon) {
        CoroutineScope(Dispatchers.IO).launch {
            persistenceRepository.updatePokemon(pokemon)
        }
    }


}
