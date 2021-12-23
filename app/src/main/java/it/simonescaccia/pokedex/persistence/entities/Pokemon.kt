package it.simonescaccia.pokedex.persistence.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Pokemon {
    @PrimaryKey
    var id: Int = 0
    var name: String = ""
    var order: Int = 0
    var pokedexnumber: Int = 0
    var color: String = ""
    var species: String = ""
    var linkImage: String = ""
    var type1: String = ""
    var type2: String? = ""

    //1 favorite, 0 not favorite
    var favorite: Int = 0

    override fun toString(): String {
        return "$id $name $favorite"
    }
}