package it.simonescaccia.pokedex.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import it.simonescaccia.pokedex.R
import it.simonescaccia.pokedex.adapters.PokemonListAdapter
import it.simonescaccia.pokedex.adapters.ViewPagerFragmentAdapter
import it.simonescaccia.pokedex.databinding.ActivityCharacteristicsBinding
import it.simonescaccia.pokedex.interfaces.CallbackFavorite
import it.simonescaccia.pokedex.listeners.FavoriteCharacteristicsClickListener
import it.simonescaccia.pokedex.parcelables.PokemonParcelable
import it.simonescaccia.pokedex.persistence.entities.Pokemon
import it.simonescaccia.pokedex.viewmodels.CharacteristicsViewModel

class CharacteristicsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharacteristicsBinding
    private lateinit var adapter: ViewPagerFragmentAdapter
    private val characteristicsViewModel: CharacteristicsViewModel by viewModels()
    private var position = 0
    private var favoritePokemon: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacteristicsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get the pokemon parcelable and position
        position = intent.extras?.getInt("position")!!
        val pokemonParcelable = intent.extras?.getParcelable<PokemonParcelable>("pokemon")!!
        setUI(pokemonParcelable.getPokemon())
    }

    private fun setUI(pokemon: Pokemon) {

        //set the callback for update the adapter of recycler views
        val callbackFavorite = object : CallbackFavorite {
            override fun onFavorite() {
                favoritePokemon = 1
            }
            override fun onNotFavorite() {
                favoritePokemon = 0
            }
        }

        //set the listener
        val listener = FavoriteCharacteristicsClickListener()
        listener.viewModel = characteristicsViewModel
        listener.pokemon = pokemon
        listener.callback = callbackFavorite

        //fill the card view
        val holder = PokemonListAdapter.ViewHolder(binding.cvPokemonitem)
        holder.clickListener = listener
        holder.pokemon = pokemon

        //set the adapter to view pager
        val bundle = Bundle()
        bundle.putParcelable("pokemon", PokemonParcelable(pokemon))
        adapter = ViewPagerFragmentAdapter(this, bundle)
        binding.vp2Fragments.adapter = adapter

        val titles = arrayOf(
            getString(R.string.abilities),
            getString(R.string.moves),
            getString(R.string.statistics)
        )

        val icons = arrayOf(
            R.drawable.ic_charm,
            R.drawable.ic_action,
            R.drawable.ic_insight
        )

        // attaching tab mediator
        TabLayoutMediator(
            binding.tabLayout, binding.vp2Fragments
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = titles[position]
            tab.icon =
                ContextCompat.getDrawable(this,
                    icons[position])
        }.attach()
    }

    /**
     * inflate option menu
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_favorite_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        //return the position to the previous activity
        val intent = Intent()
        intent.putExtra("position", position)
        intent.putExtra("favorite", favoritePokemon)
        setResult(RESULT_OK, intent)
        super.onBackPressed()
    }

}