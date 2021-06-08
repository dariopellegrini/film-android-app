package com.dariopellegrini.course

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dariopellegrini.spike.response.Spike
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.dariopellegrini.spike.*
import com.dariopellegrini.spike.network.SpikeMethod

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Spike.instance.configure(this)

        networkCall()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun networkCall() {
        CoroutineScope(Dispatchers.Main).launch {
            val film = request<Film> {
                baseURL = "http://api.tvmaze.com/"
                path = "shows/1"
                method = SpikeMethod.GET
            }
            Log.i("NetworkFilm", film.results ?: "No result")
        }
    }
}