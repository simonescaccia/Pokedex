package it.simonescaccia.pokedex.listeners

import android.view.View
import android.widget.ImageView
import it.simonescaccia.pokedex.R
import it.simonescaccia.pokedex.interfaces.CallbackFavorite
import it.simonescaccia.pokedex.viewmodels.CharacteristicsViewModel

class FavoriteCharacteristicsClickListener: FavoriteClickListener() {

    lateinit var viewModel: CharacteristicsViewModel
    lateinit var callback: CallbackFavorite

    override fun onClick(p0: View?) {
        p0 as ImageView
        if(p0.isSelected) {
            //the pokemon is already favorite so update to not favorite
            pokemon.favorite = 0
            callback.onNotFavorite()
            viewModel.update(pokemon)
            p0.setImageResource(R.drawable.ic_not_favorite_item)
        } else {
            pokemon.favorite = 1
            callback.onFavorite()
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
        val newCharacteristicsClickListener = FavoriteCharacteristicsClickListener()
        newCharacteristicsClickListener.viewModel = viewModel
        return newCharacteristicsClickListener
    }
}