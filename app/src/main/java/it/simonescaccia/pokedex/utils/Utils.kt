package it.simonescaccia.pokedex.utils

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import it.simonescaccia.pokedex.persistence.entities.Pokemon
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import kotlin.math.min

object Utils {

    /**
     * return true if the image is not an official-artwork.
     * Images that aren't official-artwork has less quality
     * So don't resize its
     */
    fun isBadImage(link: String): Boolean {
        //https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/99.png
        //is not to bad
        //https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/10061.png
        //is bad
        return !link.lowercase().contains("official-artwork")
    }

    /**
     * return a lighten color
     */
    fun lighten(color: Int, fraction: Double): Int {
        var red: Int = Color.red(color)
        var green: Int = Color.green(color)
        var blue: Int = Color.blue(color)
        red = lightenColor(red, fraction)
        green = lightenColor(green, fraction)
        blue = lightenColor(blue, fraction)
        val alpha: Int = Color.alpha(color)
        return Color.argb(alpha, red, green, blue)
    }

    private fun lightenColor(color: Int, fraction: Double): Int {
        return min(color + color * fraction, 255.0).toInt()
    }

    fun downloadImage(pokemon: Pokemon, application: Application) {
        //create the directory on internal storage if not created
        val path = application.filesDir.absolutePath
        val dirName = "/pokeimg"
        val dir = File(path, dirName)
        if(!dir.exists())
            dir.mkdir()
        val imageName = "${pokemon.name}.png"
        val file = File("${dir.absolutePath}/$imageName")
        //check if image not exists on internal storage and if is not an official-artwork
        //images that aren't official-artwork has less quality so don't resize
        if(!file.exists()) {
            //download img
            try {
                val url = URL(pokemon.linkImage)
                val iS = url.openConnection().getInputStream()
                val opts = BitmapFactory.Options()
                if (!isBadImage(pokemon.linkImage)) {
                    //request a smaller image
                    opts.inSampleSize = 2
                }
                val bitmap = BitmapFactory.decodeStream(iS, null, opts)
                val fOut = FileOutputStream(file)

                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fOut)
                fOut.flush()
                fOut.close()
            } catch (e: Exception) {
                Log.d("POKEMON", "Connection failed")
            }
        }
    }
}