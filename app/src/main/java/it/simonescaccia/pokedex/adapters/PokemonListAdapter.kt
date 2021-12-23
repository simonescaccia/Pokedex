package it.simonescaccia.pokedex.adapters

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import it.simonescaccia.pokedex.R
import it.simonescaccia.pokedex.activities.CharacteristicsActivity
import it.simonescaccia.pokedex.databinding.PokemonItemBinding
import it.simonescaccia.pokedex.listeners.FavoriteClickListener
import it.simonescaccia.pokedex.parcelables.PokemonParcelable
import it.simonescaccia.pokedex.persistence.entities.Pokemon
import it.simonescaccia.pokedex.utils.Utils
import java.io.File

class PokemonListAdapter (
    pokemonList: List<Pokemon>,
    private var clickListener: FavoriteClickListener,
    private val characteristicsLauncherActivity: ActivityResultLauncher<Intent>
): RecyclerView.Adapter<PokemonListAdapter.ViewHolder>(),
    View.OnClickListener,
    View.OnLongClickListener {

    private var allPokemonList: MutableList<Pokemon> = pokemonList as MutableList
    private var filteredPokemonList: MutableList<Pokemon> = allPokemonList

    /**
     * inflate recycler view items
     */
    class ViewHolder(
        binding: PokemonItemBinding,
        ): RecyclerView.ViewHolder(binding.root) {

        private var tvPokeNumber = binding.tvPokeNumber
        private var tvPokeName = binding.tvPokeName
        private var tvType1 = binding.tvType1
        private var tvType2 = binding.tvType2
        private var ivPokeImage = binding.ivPokeImage
        private var cvPokemon = binding.cvPokemon
        private var ivImageBack = binding.ivImageback
        private var ivFavorite = binding.ivFavorite
        lateinit var clickListener: FavoriteClickListener

        var pokemon: Pokemon = Pokemon()
            set(value) {
                field = value

                //set correct favorite image
                if(value.favorite == 1) {
                    ivFavorite.setImageResource(R.drawable.ic_favorite_item)
                    ivFavorite.isSelected = true
                } else {
                    ivFavorite.setImageResource(R.drawable.ic_not_favorite_item)
                    ivFavorite.isSelected = false
                }
                ivFavorite.setOnClickListener(clickListener)

                val context = tvPokeName.context
                val backgroundColor = Color.parseColor(value.color)
                //set text color in function of background card view color
                if (ColorUtils.calculateLuminance(backgroundColor) < 0.15) {
                    //card view color is dark
                    tvPokeNumber.setTextColor(context.getColor(R.color.text_light))
                    tvPokeName.setTextColor(context.getColor(R.color.text_light))
                    tvType1.setTextColor(context.getColor(R.color.text_light))
                    tvType2.setTextColor(context.getColor(R.color.text_light))
                    val shape = tvType1.background as GradientDrawable
                    shape.setStroke(4, context.getColor(R.color.text_light))

                    //type2 should be null, so need to remove stroke if null
                    val shape2 = tvType2.background as GradientDrawable
                    if (value.type2 != null) {
                        shape2.setStroke(4, context.getColor(R.color.text_light))
                    } else {
                        shape2.setStroke(0, Color.TRANSPARENT)
                    }

                    //set correct favorite image tint
                    ImageViewCompat.setImageTintList(
                        ivFavorite,
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.text_light)
                        )
                    )
                } else {
                    //card view color is light
                    tvPokeNumber.setTextColor(context.getColor(R.color.text_dark))
                    tvPokeName.setTextColor(context.getColor(R.color.text_dark))
                    tvType1.setTextColor(context.getColor(R.color.text_dark))
                    tvType2.setTextColor(context.getColor(R.color.text_dark))
                    val shape = tvType1.background as GradientDrawable
                    shape.setStroke(4, context.getColor(R.color.text_dark))

                    val shape2 = tvType2.background as GradientDrawable
                    if (value.type2 != null) {
                        shape2.setStroke(4, context.getColor(R.color.text_dark))
                    } else {
                        shape2.setStroke(0, Color.TRANSPARENT)
                    }

                    //set correct favorite image tint
                    ImageViewCompat.setImageTintList(
                        ivFavorite,
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.text_dark)
                        )
                    )
                }

                tvPokeNumber.text = when (value.pokedexnumber.toString().length) {
                    1 -> {
                        "#00${value.pokedexnumber}"
                    }
                    2 -> {
                        "#0${value.pokedexnumber}"
                    }
                    else -> {
                        "#${value.pokedexnumber}"
                    }
                }
                //uppercase the first letter
                tvPokeName.text = tvPokeName.context.getString(R.string.pokename,value.name.substring(0, 1).uppercase(),value.name.substring(1))

                //choose correct type language
                tvType1.text = value.type1.uppercase()
                tvType2.text = value.type2?.uppercase()

                //set image view using picasso library
                //if the image is already downloaded by the service just load it from internal storage
                //else picasso get error and download the image from internet
                //in case that the image is already in cache, managed by picasso, it doesn't need to
                //load from internal storage or download from internet
                val path = "${context.filesDir.absolutePath}/pokeimg/${value.name}.png"
                Picasso.get()
                    .load(File(path))
                    .into(ivPokeImage, object : Callback {
                        override fun onSuccess() {
                        }
                        //file not found on internal storage so retry with uri request
                        override fun onError(e: Exception) {
                            Log.d("POKEMON", "onError ${value.name}, ${e.message}")
                            Picasso.get()
                                .load(value.linkImage)
                                .into(ivPokeImage)
                        }
                    })
                cvPokemon.setCardBackgroundColor(
                    try {
                        value.color.toColorInt()
                    } catch(e: IllegalArgumentException) {
                        Color.CYAN
                    })

                //set lighten color to back pokemon image view
                val shape = ivImageBack.background as GradientDrawable
                shape.setColor(Utils.lighten(backgroundColor, 0.13))
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PokemonItemBinding.inflate(layoutInflater, parent, false)

        //let CardView react to onClick and onLongClick
        binding.cvPokemon.setOnClickListener(this)
        //set the pokemon to the listener for reuse this adapter by more Activity
        //they sets her ViewModel
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //clone the listener first to pass to ViewHolder
        val pokemonClickListener = clickListener.clone()
        pokemonClickListener.pokemon = filteredPokemonList[position]
        holder.clickListener = pokemonClickListener
        holder.pokemon = filteredPokemonList[position]
    }

    override fun getItemCount(): Int {
        return filteredPokemonList.size
    }

    fun getItem(p0: Int): Pokemon {
        return filteredPokemonList[p0]
    }

    //launch Pokemon characteristic Activity
    override fun onClick(p0: View) {
        val position = (p0.parent as RecyclerView).getChildAdapterPosition(p0)
        val pokemon: Pokemon = getItem(position)
        val pokemonParcelable = PokemonParcelable(pokemon)
        val bundle = Bundle()
        bundle.putInt("position", position)
        bundle.putParcelable("pokemon", pokemonParcelable)
        val intent = Intent(p0.context, CharacteristicsActivity::class.java)
        intent.putExtras(bundle)
        characteristicsLauncherActivity.launch(intent)
    }

    override fun onLongClick(p0: View?): Boolean {
        return true
    }

    /**
     * SearchView filter
     */
    fun getPokemonFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                filteredPokemonList = if(filterResults.values != null) {
                    filterResults.values as MutableList<Pokemon>
                } else {
                    mutableListOf()
                }
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val queryString = charSequence?.toString()?.uppercase()

                val filterResults = FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty())
                    allPokemonList
                else {
                    //search for name, Pokedex number and type
                    var resultName = allPokemonList.filter {
                        it.name.uppercase().startsWith(queryString)
                    }
                    if(resultName.isEmpty()) {
                        resultName = allPokemonList.filter {
                            it.pokedexnumber.toString().startsWith(queryString)
                        }
                    }
                    if(resultName.isEmpty()) {
                        val resultType1 = allPokemonList.filter {
                            it.type1.uppercase().startsWith(queryString)
                        }
                        val resultType2 = allPokemonList.filter {
                            //check if type2 has null value
                            if(it.type2 != null) {
                                it.type2!!.uppercase().startsWith(queryString)
                            } else {
                                false
                            }
                        }
                        resultName = resultType1 + resultType2
                    }
                    resultName
                }
                return filterResults
            }
        }
    }

    /**
     * Sort list
     */
    fun sortListBy(sortingValue: String) {
        val list: MutableList<Pokemon> = filteredPokemonList
        when (sortingValue) {
            "name" -> list.sortBy {
                it.name
            }
            "number" -> list.sortBy {
                it.pokedexnumber
            }
            "number_desc" -> list.sortByDescending {
                it.pokedexnumber
            }
            "type" -> list.sortBy {
                it.type1
            }
        }
        notifyDataSetChanged()
    }

    /**
     * remove item from list
     */
    fun remove(position: Int) {
        filteredPokemonList.removeAt(position)
        notifyItemRemoved(position)
    }

}