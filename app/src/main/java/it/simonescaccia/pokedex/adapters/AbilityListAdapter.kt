package it.simonescaccia.pokedex.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import it.simonescaccia.pokedex.R
import it.simonescaccia.pokedex.databinding.AbilityItemBinding
import it.simonescaccia.pokedex.models.Ability
import it.simonescaccia.pokedex.persistence.entities.Pokemon

class AbilityListAdapter (
    private val abilityList: List<Ability>,
    private val pokemon: Pokemon
    ): RecyclerView.Adapter<AbilityListAdapter.ViewHolder>(){

    /**
     * inflate recycler view items
     */
    class ViewHolder(binding: AbilityItemBinding): RecyclerView.ViewHolder(binding.root) {
        private var tvAbilityName = binding.tvAbilityName
        private var tvAbilityHidden = binding.tvAbilityHidden
        private var tvAbilityEffect = binding.tvAbilityDescription
        private var cvActivity = binding.cvActivity
        private val context = binding.tvAbilityName.context

        var ability: Ability = Ability()
            set(value) {
                field = value

                tvAbilityName.text = ability.name
                tvAbilityHidden.text = if (ability.isHidden)
                    context.getString(R.string.hidden)
                else
                    ""
                tvAbilityEffect.text = ability.effect
            }

        var pokemon: Pokemon = Pokemon()
            set(value) {
                field = value

                val context = tvAbilityEffect.context
                val backgroundColor = Color.parseColor(value.color)
                //set text color in function of background card view color
                if (ColorUtils.calculateLuminance(backgroundColor) < 0.15) {
                    //card view color is dark
                    tvAbilityName.setTextColor(context.getColor(R.color.text_light))
                    tvAbilityEffect.setTextColor(context.getColor(R.color.text_light))
                    tvAbilityHidden.setTextColor(context.getColor(R.color.text_light))
                } else {
                    //card view color is light
                    tvAbilityName.setTextColor(context.getColor(R.color.text_dark))
                    tvAbilityEffect.setTextColor(context.getColor(R.color.text_dark))
                    tvAbilityHidden.setTextColor(context.getColor(R.color.text_dark))

                }

                cvActivity.setCardBackgroundColor(
                    try {
                        value.color.toColorInt()
                    } catch(e: IllegalArgumentException) {
                        Color.CYAN
                    })

            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AbilityItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ability = abilityList[position]
        holder.pokemon = pokemon
    }

    override fun getItemCount(): Int {
        return abilityList.size
    }
}
