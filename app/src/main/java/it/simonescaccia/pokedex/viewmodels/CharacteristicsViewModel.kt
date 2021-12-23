package it.simonescaccia.pokedex.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import it.simonescaccia.pokedex.persistence.entities.Pokemon
import it.simonescaccia.pokedex.repositories.PersistenceSingletonRepository
import it.simonescaccia.pokedex.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CharacteristicsViewModel(application: Application): AndroidViewModel(application) {

    private var persistenceRepository = PersistenceSingletonRepository.getInstance(application)

    fun update(pokemon: Pokemon) {
        CoroutineScope(Dispatchers.IO).launch {
            persistenceRepository.updatePokemon(pokemon)
        }
        //save the image if the pokemon is favorite and if not already downloaded
        if(pokemon.favorite == 1) {
            CoroutineScope(Dispatchers.IO).launch {
                Utils.downloadImage(pokemon, getApplication())
            }
        }
    }
}
