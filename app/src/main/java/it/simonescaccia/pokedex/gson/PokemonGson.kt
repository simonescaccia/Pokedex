package it.simonescaccia.pokedex.gson

class PokemonGson {
    lateinit var abilities: List<AbilityObjectGson>
    lateinit var moves: List<MoveObjectGson>
    var height: Int = 0
    var weight: Int = 0
    lateinit var stats: List<StatObjectGson>

    class AbilityObjectGson {
        lateinit var ability: AbilityInfoGson
        var is_hidden: Boolean = false
    }

    class AbilityInfoGson {
        var name: String = ""
        var url: String = ""
    }

    class MoveObjectGson {
        lateinit var move: MoveInfoGson
    }

    class MoveInfoGson {
        var name: String = ""
        var url: String = ""
    }

    class StatObjectGson {
        var base_stat: Int = 0
        lateinit var stat: StatInfo
    }

    class StatInfo {
        var name: String = ""
        var url: String = ""
    }
}