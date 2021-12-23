package it.simonescaccia.pokedex.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import it.simonescaccia.pokedex.R
import it.simonescaccia.pokedex.adapters.PokemonListAdapter
import it.simonescaccia.pokedex.databinding.ActivityFavoriteBinding
import it.simonescaccia.pokedex.interfaces.CallbackRemoveFavoriteFromAdapter
import it.simonescaccia.pokedex.listeners.FavoriteFavoriteClickListener
import it.simonescaccia.pokedex.listeners.QueryListener
import it.simonescaccia.pokedex.persistence.entities.Pokemon
import it.simonescaccia.pokedex.viewmodels.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private var adapter: PokemonListAdapter? = null
    private var adapterPosition: Int = 0
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLiveDataObservers()
        setUI()
    }

    private fun setUI() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvPokemonFavorite.layoutManager = layoutManager
        //show progress bar to attend list of pokemon
        binding.pbPokemonFavorite.visibility = View.VISIBLE
        //inflate the recycler view
        favoriteViewModel.loadLists()
    }

    private fun setLiveDataObservers() {
        //set equivalent of onActivityResult for refreshing the adapter when
        //Characteristics activity set favorite/not favorite the pokemon
        val launchCharacteristicsActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            //refresh adapter item
            val favorite = result.data!!.extras?.getInt("favorite")!!
            if(favorite == 0) {
                //remove from adapter if is not favorite
                val position = result.data!!.extras?.getInt("position")!!
                adapter?.remove(position)
            }
        }

        //set the callback to delete from adapter the pokemon when not favorite is clicked
        val callback = object : CallbackRemoveFavoriteFromAdapter {
            override fun onRemove(positionItem: Int) {
                //remove item
                adapter?.remove(positionItem)
            }
        }

        //observe view model list
        val observer = Observer<List<Pokemon>> { list ->
            if (list != null) {
                //hide progress bar  and set recycler view adapter
                binding.pbPokemonFavorite.visibility = View.INVISIBLE

                //set the listener
                val listener = FavoriteFavoriteClickListener()
                listener.viewModel = favoriteViewModel
                listener.callbackRemoveFavoriteFromAdapter = callback

                adapter = PokemonListAdapter(list, listener, launchCharacteristicsActivity)
                //set SearchView adapter
                searchView?.setOnQueryTextListener(QueryListener(adapter!!))
                binding.rvPokemonFavorite.adapter = adapter
                binding.rvPokemonFavorite.layoutManager?.scrollToPosition(adapterPosition)
                adapterPosition = 0
            }
        }
        favoriteViewModel.pokemonList.observe(this, observer)
    }

    /**
     * device rotation safety
     */
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putInt(
            "position",
            (binding.rvPokemonFavorite.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        ) // get current recycle view position here.

        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        //restore recycler view position after device rotation
        adapterPosition = savedInstanceState.getInt("position")
    }

    /**
     * inflate option menu
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_favorite_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * action option menu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_home -> {
                //show home activity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.item_remove_all -> {
                //remove all pokemon from favorite
                val size = adapter?.itemCount
                if(size != null && size != 0) {
                    for (i in 0 until size) {
                        val pokemon = adapter?.getItem(0)
                        if (pokemon != null) {
                            pokemon.favorite = 0
                            favoriteViewModel.update(pokemon)
                            adapter?.remove(0)
                        }
                    }
                }
            }
            R.id.item_info -> {
                //show info activity
                val intent = Intent(this, InfoActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}