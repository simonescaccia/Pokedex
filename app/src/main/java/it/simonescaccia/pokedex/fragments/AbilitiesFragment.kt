package it.simonescaccia.pokedex.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import it.simonescaccia.pokedex.adapters.AbilityListAdapter
import it.simonescaccia.pokedex.databinding.FragmentAbilitiesBinding
import it.simonescaccia.pokedex.models.Ability
import it.simonescaccia.pokedex.parcelables.PokemonParcelable
import it.simonescaccia.pokedex.persistence.entities.Pokemon
import it.simonescaccia.pokedex.utils.Utils
import it.simonescaccia.pokedex.viewmodels.AbilitiesViewModel
import java.lang.Exception

class AbilitiesFragment : Fragment() {

    private lateinit var binding: FragmentAbilitiesBinding
    private lateinit var pokemon: Pokemon
    private lateinit var adapter: AbilityListAdapter
    private val abilitiesViewModel: AbilitiesViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pokemon = arguments?.getParcelable<PokemonParcelable>("pokemon")!!.getPokemon()
        binding = FragmentAbilitiesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLiveData()
        setUI()

    }

    private fun setLiveData() {
        val observer = Observer<List<Ability>> { list ->
            //hide progress bar  and set recycler view adapter
            binding.pbAbility.visibility = View.INVISIBLE

            adapter = AbilityListAdapter(list, pokemon)
            binding.rvAbilities.adapter = adapter
        }
        abilitiesViewModel.abilityList.observe(viewLifecycleOwner, observer)
    }

    private fun setUI() {
        binding.pbPokeimage.visibility = View.VISIBLE
        binding.pbAbility.visibility = View.VISIBLE

        binding.cvAbilities.setCardBackgroundColor(
            try {
                val color = pokemon.color.toColorInt()
                Utils.lighten(color, 0.13)
            } catch(e: IllegalArgumentException) {
                Color.CYAN
            })

        //set abilities recycler view
        val layoutManager = LinearLayoutManager(activity)
        binding.rvAbilities.layoutManager = layoutManager

        abilitiesViewModel.loadAbilities(pokemon)

        //set the image view
        Picasso.get()
            .load(pokemon.linkImage)
            .into(binding.ivPokeimg, object : Callback {
                override fun onSuccess() {
                    //hide progress bar
                    binding.pbPokeimage.visibility = View.INVISIBLE
                }

                override fun onError(e: Exception?) {
                }
            })
    }
}