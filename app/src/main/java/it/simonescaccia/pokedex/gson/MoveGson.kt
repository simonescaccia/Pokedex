package it.simonescaccia.pokedex.gson

class MoveGson {
    var name: String = ""
    lateinit var names: List<NameGson>
    var accuracy: Int = 0
    var power: Int = 0
    var pp: Int = 0
    lateinit var type: TypeGsonInfo
    lateinit var damage_class: DamageClassGson

    class DamageClassGson {
        var name: String = ""
    }

    class TypeGsonInfo {
        var name: String = ""
        var url: String = ""
    }
}