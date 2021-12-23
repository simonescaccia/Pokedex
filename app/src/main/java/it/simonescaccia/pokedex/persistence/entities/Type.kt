package it.simonescaccia.pokedex.persistence.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Type {
    @PrimaryKey
    var name: String = ""
    var color: String = ""
    var it: String = ""
}