package it.simonescaccia.pokedex.listeners

import androidx.appcompat.widget.SearchView
import it.simonescaccia.pokedex.adapters.PokemonListAdapter

/**
 * listener for SearchView to filter the pokemon list
 */
class QueryListener(private val adapter: PokemonListAdapter) : SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        adapter.getPokemonFilter().filter(p0)
        return false
    }
}