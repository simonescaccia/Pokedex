package it.simonescaccia.pokedex.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import it.simonescaccia.pokedex.repositories.PersistenceSingletonRepository
import it.simonescaccia.pokedex.services.ImageDownloadManagerService

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * apertura delle cannessione verso il database
         * e caricamento della lista dei pokemon da visualizzare nella main activity
         *
         */
        PersistenceSingletonRepository.getInstance(application)

        /**
         * download e salvataggio nell'internal storage delle immagini dei pokemon in background
         */
        val intent = Intent(this, ImageDownloadManagerService::class.java)
        startService(intent)

        /**
         * il layout è dichiarato direttamente nel Manifest tramite un tema
         */
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}