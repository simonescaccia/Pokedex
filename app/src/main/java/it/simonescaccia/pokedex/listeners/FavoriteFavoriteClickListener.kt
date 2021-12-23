package it.simonescaccia.pokedex.listeners

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import it.simonescaccia.pokedex.interfaces.CallbackRemoveFavoriteFromAdapter
import it.simonescaccia.pokedex.viewmodels.FavoriteViewModel

class FavoriteFavoriteClickListener: FavoriteClickListener() {

    lateinit var viewModel: FavoriteViewModel
    lateinit var callbackRemoveFavoriteFromAdapter: CallbackRemoveFavoriteFromAdapter

    override fun onClick(p0: View?) {
        p0 as ImageView
        val position = (p0.parent.parent.parent as RecyclerView).getChildAdapterPosition(p0.parent.parent as View)
        //the pokemon is already favorite so update to not favorite
        pokemon.favorite = 0
        callbackRemoveFavoriteFromAdapter.onRemove(position)
        viewModel.update(pokemon)
    }

    /**
     * return a new object with the same property
     *
     * Every CardView has a different listener object because the pokemon that need to update is different
     */
    override fun clone(): FavoriteClickListener {
        val newFavoriteClickListener = FavoriteFavoriteClickListener()
        newFavoriteClickListener.viewModel = viewModel
        newFavoriteClickListener.callbackRemoveFavoriteFromAdapter = callbackRemoveFavoriteFromAdapter
        return newFavoriteClickListener
    }

}