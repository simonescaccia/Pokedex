package it.simonescaccia.pokedex.models

import it.simonescaccia.pokedex.gson.PokemonGson
import it.simonescaccia.pokedex.gson.StatGson
import java.util.*

class Stat {
    var name: String = ""
    var value: Int = 0
    var id: Int = 0

    constructor()

    constructor(statGson: StatGson, statObjectGson: PokemonGson.StatObjectGson) {
        //default name of stat
        name = statObjectGson.stat.name
        id = statGson.id
        value = statObjectGson.base_stat
        if(Locale.getDefault().language.equals("it")) {
            for(statName in statGson.names) {
                if(statName.language.name == "it") {
                    name = statName.name
                }
            }
            //it translation doesn't found
            if(name.isEmpty())
                name = statGson.name
        } else
            name = statGson.name
    }
}