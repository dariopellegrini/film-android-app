package com.dariopellegrini.course

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dariopellegrini.course.row.FilmRow
import com.dariopellegrini.declarativerecycler.RecyclerManager
import com.dariopellegrini.spike.*
import com.dariopellegrini.spike.mapping.mappingThrowable
import com.dariopellegrini.spike.mapping.suspend
import com.dariopellegrini.spike.network.SpikeMethod
import com.dariopellegrini.spike.response.Spike
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    var recyclerManager: RecyclerManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Spike.instance.configure(this)

        recyclerManager = RecyclerManager(recyclerView, LinearLayoutManager(this))

        listNetworkCall()

        val list = listOf<Any>("1", "2", "3", 1)
        val mutatingList = mutableListOf<Any>("1", "2", "3", 1)
        val map = mutableMapOf<String, Int>("s1" to 1, "s2" to 2)
        map["s3"] = 5
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun listNetworkCall() = CoroutineScope(Dispatchers.Main).launch {
        try {
            val containers = request<List<FilmContainer>> {
                baseURL = "https://api.tvmaze.com/"
                path = "schedule"
                method = SpikeMethod.GET
            }.suspend.mappingThrowable()

            val films = containers.map { it.show }

            recyclerManager?.push(
                films.map { FilmRow(it) {
                    // open film detail
                 }
                }
            )
            recyclerManager?.reload()

            Log.i("NetworkFilm", films.toString())
        } catch (e: Exception) {
            Log.e("NetworkFilm", "$e")
        }
    }

//    fun networkCall() {
//        CoroutineScope(Dispatchers.Main).launch {
//
//            try {
//                val film = request<Film> {
//                    baseURL = "http://api.tvmaze.com/"
//                    path = "shows/1"
//                    method = SpikeMethod.GET
//                }.suspend.mappingThrowable()
//
//                titleTextView.text = film.name
//                languageTextView.text = film.language
//                durationTextView.text = "Durata ${film.runtime}"
//                genresTextView.text = film.genres
//                        .filter { it.length > 5 }
//                        .map { "$it (${it.length})" }
//                        .joinToString(" - ")
//
//                Glide.with(this@MainActivity)
//                        .load(film.image.original)
//                        .into(imageView)
//
//                websiteButton.setOnClickListener {
//                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(film.url))
//                    startActivity(browserIntent)
//                }
//
//                Log.i("NetworkFilm", film.toString())
//            } catch (e: Exception) {
//                Log.e("NetworkFilm", "$e")
//            }
//        }
//    }
}