package it.simonescaccia.pokedex.listeners

import android.view.View
import android.widget.ImageView
import it.simonescaccia.pokedex.R
import it.simonescaccia.pokedex.viewmodels.PokemonListViewModel

/**
 * specific click listener for MainActivity
 */
class FavoriteMainClickListener: FavoriteClickListener() {

    lateinit var viewModel: PokemonListViewModel

    override fun onClick(p0: View?) {
        p0 as ImageView
        if(p0.isSelected) {
            //the pokemon is already favorite so update to not favorite
            pokemon.favorite = 0
            viewModel.update(pokemon)
            p0.setImageResource(R.drawable.ic_not_favorite_item)
        } else {
            pokemon.favorite = 1
            viewModel.update(pokemon)
            p0.setImageResource(R.drawable.ic_favorite_item)
        }
        p0.isSelected = !p0.isSelected
    }

    /**
     * return a new object with the same property
     *
     * Every ViewHolder has a different listener object
     */
    override fun clone(): FavoriteClickListener {
        val newMainClickListener = FavoriteMainClickListener()
        newMainClickListener.viewModel = viewModel
        return newMainClickListener
    }

}