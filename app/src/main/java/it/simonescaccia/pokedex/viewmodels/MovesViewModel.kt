package it.simonescaccia.pokedex.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import it.simonescaccia.pokedex.gson.PokemonGson
import it.simonescaccia.pokedex.interfaces.CallbackMoveList
import it.simonescaccia.pokedex.interfaces.CallbackMove
import it.simonescaccia.pokedex.models.Move
import it.simonescaccia.pokedex.persistence.entities.Pokemon
import it.simonescaccia.pokedex.repositories.PersistenceSingletonRepository
import it.simonescaccia.pokedex.repositories.VolleySingletonRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovesViewModel(application: Application): AndroidViewModel(application) {

    private val volleyRepository = VolleySingletonRepository.getInstance(application)
    private val persistenceRepository = PersistenceSingletonRepository.getInstance(application)
    var moveList: MutableLiveData<MutableList<Move>> = MutableLiveData()

    //callbacks result list
    private lateinit var listCallbackMove: MutableList<Move>
    private var listSize = 0
    private var count = 0

    fun loadMoves(pokemon: Pokemon) {
        listCallbackMove = mutableListOf()
        CoroutineScope(Dispatchers.IO).launch {
            /**
             * set the pokemon moves
             */
            volleyRepository.getMovesGson(pokemon, callbackMoveList)
        }
    }

    //callbacks
    private val callbackMoveList = object : CallbackMoveList {
        override suspend fun onSuccess(moveListGson: List<PokemonGson.MoveObjectGson>?) {
            if (moveListGson == null || moveListGson.isEmpty()) {
                //no moves specified for this Pokemon
                val move = Move()
                move.name = "empty"
                //Cannot invoke setValue on a background thread with moveList.value, need postValue
                moveList.postValue(mutableListOf(move))
                return
            }
            /**
             * for each move listed on pokemon info get the full Move Object
             */
            listSize = moveListGson.size
            for (moveObjectGson in moveListGson) {
                volleyRepository.getMove(
                    moveObjectGson,
                    persistenceRepository.getListOfType(),
                    callbackMove)
            }
        }

        override fun onError(error: String) {
            Log.d("POKEMON", error)
        }
    }

    private val callbackMove = object : CallbackMove {
        override fun onSuccess(move: Move) {
            listCallbackMove.add(move)
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
            moveList.value = listCallbackMove
        }
    }
}
