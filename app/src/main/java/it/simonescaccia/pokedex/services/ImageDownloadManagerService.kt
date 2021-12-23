package it.simonescaccia.pokedex.services

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.HandlerThread
import android.os.IBinder
import android.os.Process.THREAD_PRIORITY_BACKGROUND
import android.util.Log
import it.simonescaccia.pokedex.repositories.PersistenceSingletonRepository
import it.simonescaccia.pokedex.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class ImageDownloadManagerService : Service() {

    private lateinit var job: Job

    override fun onBind(intent: Intent): IBinder? {
        // This is a started service, We don't provide binding so return null
        return null
    }

    override fun onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread("ServiceStartArguments", THREAD_PRIORITY_BACKGROUND).apply {
                start()
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        job = CoroutineScope(Dispatchers.IO).launch {
            //create the directory on internal storage that will contains all pokemon images
            val path = applicationContext.filesDir.absolutePath
            val dirName = "/pokeimg"
            val dir = File(path, dirName)
            if(!dir.exists())
                dir.mkdir()
            //get the list of pokemon when they are loaded from repository
            val listOfPokemon =
                PersistenceSingletonRepository.getInstance(applicationContext).getListOfPokemon()
            //download all the image for each pokemon in the list, but only if the image is
            //not present in the directory
            for(pokemon in listOfPokemon) {
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
                        if (!Utils.isBadImage(pokemon.linkImage)) {
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
                        break
                    }
                }
            }
            stopSelf()
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        // stop job coroutine because has no lifecycle parent
        job.cancel()
    }

}