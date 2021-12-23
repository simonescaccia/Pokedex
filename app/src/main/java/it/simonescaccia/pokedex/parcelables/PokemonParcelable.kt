package it.simonescaccia.pokedex.parcelables

import android.os.Parcel
import android.os.Parcelable
import it.simonescaccia.pokedex.persistence.entities.Pokemon

class PokemonParcelable (
    private val id: Int,
    private val name: String?,
    private val order: Int,
    private val pokedexnumber: Int,
    private val color: String?,
    private val species: String?,
    private val linkImages: String?,
    private val type1: String?,
    private val type2: String?,
    private val favorite: Int
    ): Parcelable {

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<PokemonParcelable> {
            override fun createFromParcel(parcel: Parcel) = PokemonParcelable(parcel)
            override fun newArray(size: Int) = arrayOfNulls<PokemonParcelable>(size)
        }
    }

    private constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        name = parcel.readString(),
        order = parcel.readInt(),
        pokedexnumber = parcel.readInt(),
        color = parcel.readString(),
        species = parcel.readString(),
        linkImages = parcel.readString(),
        type1 = parcel.readString(),
        type2 = parcel.readString(),
        favorite = parcel.readInt()
        )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(order)
        parcel.writeInt(pokedexnumber)
        parcel.writeString(color)
        parcel.writeString(species)
        parcel.writeString(linkImages)
        parcel.writeString(type1)
        parcel.writeString(type2)
        parcel.writeInt(favorite)
    }

    override fun describeContents() = 0

    //parcelable to Pokemon and vice versa
    constructor(pokemon: Pokemon) : this(
        pokemon.id,
        pokemon.name,
        pokemon.order,
        pokemon.pokedexnumber,
        pokemon.color,
        pokemon.species,
        pokemon.linkImage,
        pokemon.type1,
        pokemon.type2,
        pokemon.favorite
    )

    fun getPokemon(): Pokemon {
        val pokemon = Pokemon()
        pokemon.id = id
        pokemon.name = name!!
        pokemon.order = order
        pokemon.pokedexnumber = pokedexnumber
        pokemon.color = color!!
        pokemon.species = species!!
        pokemon.linkImage = linkImages!!
        pokemon.type1 = type1!!
        pokemon.type2 = type2
        pokemon.favorite = favorite

        return pokemon
    }

    override fun toString(): String {
        return "$id $name $order $pokedexnumber $color $species $linkImages $type1 $type2 $favorite"
    }

}