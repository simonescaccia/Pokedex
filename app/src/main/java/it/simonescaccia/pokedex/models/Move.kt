package it.simonescaccia.pokedex.models

import it.simonescaccia.pokedex.gson.MoveGson
import it.simonescaccia.pokedex.persistence.entities.Type
import java.util.*

class Move {
    var name = ""
    var type = ""
    var pp = 0
    var accuracy = 0
    var power = 0
    var damageClass = ""

    constructor()

    constructor(moveGson: MoveGson, listOfType: List<Type>) {
        power = moveGson.power
        accuracy = moveGson.accuracy
        pp = moveGson.pp
        damageClass = moveGson.damage_class.name
        //set the correct language
        if(Locale.getDefault().language.equals("it")) {
            for(type in listOfType) {
                if(type.name.lowercase() == moveGson.type.name.lowercase())
                    this.type = type.it
            }
        } else
            this.type = moveGson.type.name

        if(Locale.getDefault().language.equals("it")) {
            for(name in moveGson.names) {
                if(name.language.name == "it")
                    this.name = name.name
            }
            //it language not found
            if(this.name == "")
                this.name = moveGson.name
        } else {
            this.name = moveGson.name
        }
    }
}
