package it.simonescaccia.pokedex.models

import it.simonescaccia.pokedex.gson.AbilityGson
import java.util.*

class Ability {
    var name: String = ""
    var isHidden: Boolean = false
    var effect: String = ""

    constructor()

    constructor(abilityGson: AbilityGson, isHidden: Boolean) {
        //choose the correct language of ability name
        val lan = Locale.getDefault().language
        for(language in abilityGson.names) {
            if(language.language.name == lan) {
                name = language.name
                break
            }
        }
        this.isHidden = isHidden
        //choose the correct language of ability effect and translate with an external library if not found
        var itExists = false
        var englishEffect: String? = null
        for(effect in abilityGson.effect_entries) {

            if(effect.language.equals(lan)) {
                itExists = true
                this.effect = effect.effect
                break
            }
            //save the english value
            if(effect.language.name == "en") {
                englishEffect = effect.effect
            }
        }
        //error is true if translation fail
        var error = false
        if(!itExists) {
            //need to translate from english
            error = true
        }
        //set english to default
        if(error) {
            if(englishEffect == null) {
                if (abilityGson.effect_entries.isNotEmpty())
                    this.effect = abilityGson.effect_entries[0].effect
            } else {
                this.effect = englishEffect
            }
        }
    }
}
