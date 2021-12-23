package it.simonescaccia.pokedex.adapters

import android.graphics.Color
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import it.simonescaccia.pokedex.R
import it.simonescaccia.pokedex.databinding.StatItemBinding
import it.simonescaccia.pokedex.models.Stat
import it.simonescaccia.pokedex.persistence.entities.Pokemon
import it.simonescaccia.pokedex.utils.Utils

class StatListAdapter(
    private val statList: List<Stat>,
    private val pokemon: Pokemon
): RecyclerView.Adapter<StatListAdapter.ViewHolder>() {

    /**
     * inflate recycler view items
     */
    class ViewHolder(binding: StatItemBinding): RecyclerView.ViewHolder(binding.root) {

        private val tvNameStat = binding.tvNameStat
        private val pbStatValue = binding.pbStatValue
        private val tvStatValue = binding.tvStatValue
        private val context = binding.tvNameStat.context

        var stat: Stat = Stat()
            set(value) {
                field = value

                //max value of base stat
                pbStatValue.max = 255

                tvNameStat.text = value.name
                pbStatValue.setProgress(value.value, true)
                tvStatValue.text = value.value.toString()
            }

        var pokemon: Pokemon = Pokemon()
            set(value) {
                field = value

                val shape = GradientDrawable()

                val backgroundColor = Color.parseColor(value.color)
                //set text color in function of background card view color
                if (ColorUtils.calculateLuminance(backgroundColor) < 0.15) {
                    //card view color is dark
                    tvNameStat.setTextColor(context.getColor(R.color.text_light))
                    tvStatValue.setTextColor(context.getColor(R.color.text_light))
                    shape.setColor(context.getColor(R.color.text_light))
                } else {
                    //card view color is light
                    tvNameStat.setTextColor(context.getColor(R.color.text_dark))
                    tvStatValue.setTextColor(context.getColor(R.color.text_dark))
                    shape.setColor(context.getColor(R.color.text_dark))
                }

                //set the progress bar color
                val item1 = GradientDrawable()
                item1.shape = GradientDrawable.RECTANGLE
                item1.setColor(Utils.lighten(backgroundColor, 0.13))
                val item2 = ClipDrawable(shape, Gravity.START, ClipDrawable.HORIZONTAL)
                val array = arrayOf(item1, item2)
                val listDrawable = LayerDrawable(array)

                pbStatValue.progressDrawable = listDrawable
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = StatItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.stat = statList[position]
        holder.pokemon = pokemon
    }

    override fun getItemCount(): Int {
        return statList.size
    }
}
