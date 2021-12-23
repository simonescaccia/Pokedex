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
import it.simonescaccia.pokedex.adapters.StatListAdapter
import it.simonescaccia.pokedex.databinding.FragmentStatisticsBinding
import it.simonescaccia.pokedex.models.Stat
import it.simonescaccia.pokedex.models.StatsGeneral
import it.simonescaccia.pokedex.parcelables.PokemonParcelable
import it.simonescaccia.pokedex.persistence.entities.Pokemon
import it.simonescaccia.pokedex.utils.Utils
import it.simonescaccia.pokedex.viewmodels.StatsViewModel

class StatisticsFragment : Fragment() {

    private lateinit var binding: FragmentStatisticsBinding
    private lateinit var pokemon: Pokemon
    private lateinit var adapter: StatListAdapter
    private val statsViewModel: StatsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pokemon = arguments?.getParcelable<PokemonParcelable>("pokemon")!!.getPokemon()
        binding = FragmentStatisticsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLiveData()
        setUI()
    }

    private fun setLiveData() {
        val observerBaseStats = Observer<List<Stat>> { list ->
            //hide progress bar
            binding.pbStats.visibility = View.INVISIBLE

            //sort list by id so they are inflated with same order
            val listSorted = list.sortedBy {
                it.id
            }

            adapter = StatListAdapter(listSorted, pokemon)
            binding.rvBaseStats.adapter = adapter
        }
        statsViewModel.statList.observe(viewLifecycleOwner, observerBaseStats)

        val observerGeneralStats = Observer<StatsGeneral> { stats ->
            //hide progress bar
            binding.pbWeHe.visibility = View.INVISIBLE

            //fill the statistics card view
            fillGeneralStats(stats)
        }
        statsViewModel.statsGeneral.observe(viewLifecycleOwner, observerGeneralStats)
    }

    private fun fillGeneralStats(stats: StatsGeneral) {
        binding.tvWeightValue.text = getString(R.string.kg, stats.weight)
        binding.tvHeightValue.text = getString(R.string.m, stats.height)
    }

    private fun setUI() {
        binding.pbWeHe.visibility = View.VISIBLE
        binding.pbStats.visibility = View.VISIBLE

        val backgroundColor = Color.parseColor(pokemon.color)
        //set text color in function of background card view color
        if (ColorUtils.calculateLuminance(backgroundColor) < 0.15) {
            //card view color is dark
            binding.tvWeightValue.setTextColor(binding.tvWeightValue.context.getColor(R.color.text_light))
            binding.tvHeightValue.setTextColor(binding.tvWeightValue.context.getColor(R.color.text_light))
            binding.tvWeightTitle.setTextColor(binding.tvWeightValue.context.getColor(R.color.text_light))
            binding.tvHeightTitle.setTextColor(binding.tvWeightValue.context.getColor(R.color.text_light))
        } else {
            //card view color is light
            binding.tvWeightValue.setTextColor(binding.tvWeightValue.context.getColor(R.color.text_dark))
            binding.tvHeightValue.setTextColor(binding.tvWeightValue.context.getColor(R.color.text_dark))
            binding.tvWeightTitle.setTextColor(binding.tvWeightValue.context.getColor(R.color.text_dark))
            binding.tvHeightTitle.setTextColor(binding.tvWeightValue.context.getColor(R.color.text_dark))

        }

        binding.cvStatsBase.setCardBackgroundColor(
            try {
                val color = pokemon.color.toColorInt()
                Utils.lighten(color, 0.13)
            } catch (e: IllegalArgumentException) {
                Color.CYAN
            }
        )

        binding.cvStatsWeEh.setCardBackgroundColor(
            try {
                val color = pokemon.color.toColorInt()
                Utils.lighten(color, 0.13)
            } catch (e: IllegalArgumentException) {
                Color.CYAN
            }
        )

        //set stats recycler view
        val layoutManager = LinearLayoutManager(activity)
        binding.rvBaseStats.layoutManager = layoutManager

        statsViewModel.loadStats(pokemon)
    }
}