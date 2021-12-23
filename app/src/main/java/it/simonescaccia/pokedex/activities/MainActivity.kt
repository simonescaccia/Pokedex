package it.simonescaccia.pokedex.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import it.simonescaccia.pokedex.R
import it.simonescaccia.pokedex.adapters.PokemonListAdapter
import it.simonescaccia.pokedex.databinding.ActivityMainBinding
import it.simonescaccia.pokedex.listeners.FavoriteMainClickListener
import it.simonescaccia.pokedex.listeners.QueryListener
import it.simonescaccia.pokedex.persistence.entities.Pokemon
import it.simonescaccia.pokedex.viewmodels.PokemonListViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var adapter: PokemonListAdapter? = null
    private var adapterPosition: Int = 0
    private val pokemonListVM: PokemonListViewModel by viewModels()
    private var searchView: SearchView? = null
    private var searchViewText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLiveDataObservers()
        setUI()
    }

    private fun setUI() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvPokemon.layoutManager = layoutManager
        //show progress bar to attend list of pokemon
        binding.pbPokemonlist.visibility = View.VISIBLE
        //inflate the recycler view
        pokemonListVM.loadLists()
    }

    private fun setLiveDataObservers() {
        //set equivalent of onActivityResult for refreshing the adapter when
        //Characteristics activity set favorite/not favorite the pokemon
        val launchCharacteristicsActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            //refresh adapter item
            val favorite = result.data!!.extras?.getInt("favorite")!!
            //-1 is for not edited
            if(favorite != -1) {
                val position = result.data!!.extras?.getInt("position")!!
                val pokemon = adapter?.getItem(position)
                (pokemon as Pokemon).favorite = favorite
                adapter?.notifyItemChanged(position)
            }
        }

        //observe view model list
        val observer = Observer<List<Pokemon>> { list ->
            if (list != null) {
                //hide progress bar  and set recycler view adapter
                binding.pbPokemonlist.visibility = View.INVISIBLE

                //set the favorite onClick listener
                val listener = FavoriteMainClickListener()
                listener.viewModel = pokemonListVM
                adapter = PokemonListAdapter(list, listener, launchCharacteristicsActivity)
                //set SearchView adapter
                searchView?.setOnQueryTextListener(QueryListener(adapter!!))

                binding.rvPokemon.adapter = adapter
                binding.rvPokemon.layoutManager?.scrollToPosition(adapterPosition)
                adapterPosition = 0
            }
        }
        pokemonListVM.pokemonList.observe(this, observer)
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
            (binding.rvPokemon.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        ) // get current recycle view position here.

        //save the SearchView text
        savedInstanceState.putString(
            "searchViewText",
            searchView!!.query.toString()
        )
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        //restore recycler view position after device rotation
        adapterPosition = savedInstanceState.getInt("position")
        searchViewText = savedInstanceState.getString("searchViewText")
    }

    /**
     * inflate option menu
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        val searchItem = menu.findItem(R.id.item_search)
        searchView = searchItem.actionView as SearchView
        //set SearchView adapter
        if(adapter != null) {
            searchView!!.setOnQueryTextListener(QueryListener(adapter!!))
        }
        if(searchViewText != null && searchViewText.toString() != "") {

            searchView!!.isIconified = false
            searchView!!.setQuery(searchViewText, true)
        }
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * action option menu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_favorite -> {
                //show favorite activity
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
            R.id.item_info -> {
                //show info activity
                val intent = Intent(this, InfoActivity::class.java)
                startActivity(intent)
            }
            R.id.item_sort_by_name -> {
                //perform sorting
                adapter!!.sortListBy("name")
            }
            R.id.item_sort_by_number -> {
                //perform sorting
                adapter!!.sortListBy("number")
            }
            R.id.item_sort_by_number_desc -> {
                //perform sorting
                adapter!!.sortListBy("number_desc")
            }
            R.id.item_sort_by_type -> {
                //perform sorting
                adapter!!.sortListBy("type")
            }

        }
        return super.onOptionsItemSelected(item)
    }
}
