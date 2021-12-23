package it.simonescaccia.pokedex.adapters

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import it.simonescaccia.pokedex.R
import it.simonescaccia.pokedex.databinding.MoveItemBinding
import it.simonescaccia.pokedex.models.Move
import it.simonescaccia.pokedex.persistence.entities.Pokemon

class MovesListAdapter(
    private val moveList: List<Move>,
    private val pokemon: Pokemon
):
RecyclerView.Adapter<MovesListAdapter.ViewHolder>() {

    /**
     * inflate recycler view items
     */
    class ViewHolder(binding: MoveItemBinding): RecyclerView.ViewHolder(binding.root) {
        private var tvMoveName = binding.tvMoveName
        private var tvMoveType = binding.tvMoveType
        private var tvPowerValue = binding.tvPowerValue
        private var tvPpValue = binding.tvPpValue
        private var tvAccuracyValue = binding.tvAccuracyValue
        private var tvMoveDamageClass = binding.tvMoveDamageClass
        private val cvMoveInfo = binding.cvMoveInfo


        var move: Move = Move()
            set(value) {
                field = value

                tvMoveName.text = value.name
                tvMoveType.text = value.type.uppercase()
                tvAccuracyValue.text = value.accuracy.toString()
                tvPowerValue.text = value.power.toString()
                tvPpValue.text = value.pp.toString()
                tvMoveDamageClass.text = value.damageClass

            }

        var pokemon: Pokemon = Pokemon()
            set(value) {
                field = value

                val context = tvMoveName.context
                val backgroundColor = Color.parseColor(value.color)
                //set text color in function of background card view color
                if (ColorUtils.calculateLuminance(backgroundColor) < 0.15) {
                    //card view color is dark
                    tvMoveName.setTextColor(context.getColor(R.color.text_light))
                    tvMoveType.setTextColor(context.getColor(R.color.text_light))
                    tvPowerValue.setTextColor(context.getColor(R.color.text_light))
                    tvAccuracyValue.setTextColor(context.getColor(R.color.text_light))
                    tvPpValue.setTextColor(context.getColor(R.color.text_light))
                    tvMoveDamageClass.setTextColor(context.getColor(R.color.text_light))
                    val shape = tvMoveType.background as GradientDrawable
                    shape.setStroke(4, context.getColor(R.color.text_light))

                } else {
                    //card view color is light
                    tvMoveName.setTextColor(context.getColor(R.color.text_dark))
                    tvMoveType.setTextColor(context.getColor(R.color.text_dark))
                    tvPowerValue.setTextColor(context.getColor(R.color.text_dark))
                    tvAccuracyValue.setTextColor(context.getColor(R.color.text_dark))
                    tvPpValue.setTextColor(context.getColor(R.color.text_dark))
                    tvMoveDamageClass.setTextColor(context.getColor(R.color.text_dark))
                    val shape = tvMoveType.background as GradientDrawable
                    shape.setStroke(4, context.getColor(R.color.text_dark))

                }

                cvMoveInfo.setCardBackgroundColor(
                    try {
                        value.color.toColorInt()
                    } catch(e: IllegalArgumentException) {
                        Color.CYAN
                    })

            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MoveItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.move = moveList[position]
        holder.pokemon = pokemon
    }

    override fun getItemCount(): Int {
        return moveList.size
    }
}
