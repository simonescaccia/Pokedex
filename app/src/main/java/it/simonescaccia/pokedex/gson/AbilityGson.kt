package it.simonescaccia.pokedex.gson

class AbilityGson {
    lateinit var effect_entries: List<EffectGson>
    var name: String = ""
    lateinit var names: List<NameGson>

    class EffectGson {
        var effect: String = ""
        lateinit var language: LanguageGson
    }
}