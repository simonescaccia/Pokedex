package it.simonescaccia.pokedex.listeners

import android.view.View
import it.simonescaccia.pokedex.persistence.entities.Pokemon

/**
 * This class is needed to reuse the ViewHolder that inflate the CardViews.
 * The MainActivity, CharacteristicsActivity and the FavoriteActivity has all a CardView.
 * These activities needs to handle a click to the favorite image view and delegate to her ViewModel
 * to update to database.
 * The Adapter can't get information of specific Activity so this class is the generic click listener
 * and can make the Adapter set the pokemon
 *
 * So just pass the specific listener from the activity to the adapter
 *
 * Activities knows the ViewModel
 * ViewHolder know the pokemon to inflate
 */
abstract class FavoriteClickListener: View.OnClickListener {
    lateinit var pokemon: Pokemon

    /**
     * create new object of sub class
     */
    abstract fun clone(): FavoriteClickListener
}