package it.simonescaccia.pokedex.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import it.simonescaccia.pokedex.R
import it.simonescaccia.pokedex.adapters.MovesListAdapter
import it.simonescaccia.pokedex.databinding.FragmentMovesBinding
import it.simonescaccia.pokedex.models.Move
import it.simonescaccia.pokedex.parcelables.PokemonParcelable
import it.simonescaccia.pokedex.persistence.entities.Pokemon
import it.simonescaccia.pokedex.utils.Utils
import it.simonescaccia.pokedex.viewmodels.MovesViewModel

class MovesFragment : Fragment() {

    private lateinit var binding: FragmentMovesBinding
    private lateinit var pokemon: Pokemon
    private lateinit var adapter: MovesListAdapter
    private val movesViewModel: MovesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pokemon = arguments?.getParcelable<PokemonParcelable>("pokemon")!!.getPokemon()
        binding = FragmentMovesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLiveData()
        setUI()
    }

    private fun setLiveData() {
        val observer = Observer<List<Move>> { list ->
            //hide progress bar  and set recycler view adapter
            binding.pbMoves.visibility = View.INVISIBLE
            //check if list of moves is empty, equals to the list with single item and name is "empty"
            if(list.size == 1 && list[0].name == "empty") {
                binding.tvEmptyMoveList.visibility = View.VISIBLE
            } else {
                binding.tvEmptyMoveList.visibility = View.INVISIBLE
                adapter = MovesListAdapter(list, pokemon)
                binding.rvMoves.adapter = adapter
            }
        }
        movesViewModel.moveList.observe(viewLifecycleOwner, observer)
    }

    private fun setUI() {
        binding.pbMoves.visibility = View.VISIBLE

        binding.cvMoves.setCardBackgroundColor(
            try {
                val color = pokemon.color.toColorInt()
                Utils.lighten(color, 0.13)
            } catch(e: IllegalArgumentException) {
                Color.CYAN
            })
        binding.cvMovesInfo.cvMovesInfo.setCardBackgroundColor(
            try {
                pokemon.color.toColorInt()
            } catch(e: IllegalArgumentException) {
                Color.CYAN
            })
        val context = binding.tvMoves.context
        val backgroundColor = Color.parseColor(pokemon.color)
        //set text color in function of background card view color
        if (ColorUtils.calculateLuminance(backgroundColor) < 0.15) {
            //card view color is dark
            binding.cvMovesInfo.tvAccuracy.setTextColor(context.getColor(R.color.text_light))
            binding.cvMovesInfo.tvPower.setTextColor(context.getColor(R.color.text_light))
            binding.cvMovesInfo.tvPp.setTextColor(context.getColor(R.color.text_light))
            binding.cvMovesInfo.tvDamageClass.setTextColor(context.getColor(R.color.text_light))
            binding.tvEmptyMoveList.setTextColor(context.getColor(R.color.text_light))
        } else {
            //card view color is light
            binding.cvMovesInfo.tvAccuracy.setTextColor(context.getColor(R.color.text_dark))
            binding.cvMovesInfo.tvPower.setTextColor(context.getColor(R.color.text_dark))
            binding.cvMovesInfo.tvPp.setTextColor(context.getColor(R.color.text_dark))
            binding.cvMovesInfo.tvDamageClass.setTextColor(context.getColor(R.color.text_dark))
            binding.tvEmptyMoveList.setTextColor(context.getColor(R.color.text_dark))
        }

        //set abilities recycler view
        val layoutManager = LinearLayoutManager(activity)
        binding.rvMoves.layoutManager = layoutManager

        movesViewModel.loadMoves(pokemon)
    }

}