package it.simonescaccia.pokedex.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import it.simonescaccia.pokedex.activities.CharacteristicsActivity
import it.simonescaccia.pokedex.fragments.AbilitiesFragment
import it.simonescaccia.pokedex.fragments.MovesFragment
import it.simonescaccia.pokedex.fragments.StatisticsFragment

class ViewPagerFragmentAdapter(
    characteristicsActivity: CharacteristicsActivity,
    val bundle: Bundle
):
    FragmentStateAdapter(characteristicsActivity) {

    private val numberOfFragments = 3

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            1 -> MovesFragment()
            2 -> StatisticsFragment()
            else -> AbilitiesFragment()
        }
        fragment.arguments = bundle
        return fragment
    }

    override fun getItemCount(): Int {
        return numberOfFragments
    }
}
