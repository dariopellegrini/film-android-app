package com.dariopellegrini.course

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dariopellegrini.course.adapter.FilmsAdapter
import com.dariopellegrini.course.row.FilmRow
import com.dariopellegrini.declarativerecycler.BasicRow
import com.dariopellegrini.declarativerecycler.RecyclerManager
import com.dariopellegrini.spike.*
import com.dariopellegrini.spike.mapping.mappingThrowable
import com.dariopellegrini.spike.mapping.suspend
import com.dariopellegrini.spike.network.SpikeMethod
import com.dariopellegrini.spike.response.Spike
import com.dariopellegrini.storagedone.StorageDoneDatabase
import com.dariopellegrini.storagedone.get
import com.dariopellegrini.storagedone.insertOrUpdate
import com.dariopellegrini.storagedone.query.and
import com.dariopellegrini.storagedone.query.equal
import com.dariopellegrini.storagedone.sorting.ascending
import com.dariopellegrini.storagedone.suspending
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row_film.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    val recyclerManager: RecyclerManager by lazy {
        RecyclerManager(recyclerView, LinearLayoutManager(this))
    }

    val database: StorageDoneDatabase by lazy {
        StorageDoneDatabase("films")
    }

    var adapter: FilmsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Spike.instance.configure(this)
        StorageDoneDatabase.configure(this)

        val person1 = Person(1, "Dario", "P")
        val person2 = Person(2, "Nicola", "P")
        val person3 = Person(3, "Davide", "P")

        CoroutineScope(Dispatchers.Main).launch {
            try {
//                database.delete<Person>()
//
//                database.live<Person> {
//                    people ->
//                    println(people)
//                }

                DatabaseRepository.database.suspending.insertOrUpdate(person1)
                database.suspending.insertOrUpdate(person2)
                database.suspending.insertOrUpdate(person3)
                val people: List<Person> = database.suspending.get("configuration.name" equal true)
                println(people)
            } catch (e: Exception) {
                println(e)
            }
        }


        listNetworkCall()

        val sharedPreferences = getSharedPreferences("Course", 0)
        val visited = sharedPreferences.getBoolean("visited", false)

        if (!visited) {
            sharedPreferences.edit().putBoolean("visited", true).apply()
        } else {
            Log.i("Visited", "Already visited")
        }
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

            val rows = films.map { film ->
                BasicRow(
                        R.layout.row_film,
                        configuration =  {
                            itemView, position ->
                            itemView.titleRowTextView.text = film.name
                            Glide.with(itemView).load(film.image.medium).into(itemView.imageView)

                            itemView.setOnClickListener {
                                openDetail(film)
                            }
                        }
                )
            }

            recyclerManager.push(rows)
            recyclerManager.reload()

            Log.i("NetworkFilm", films.toString())
        } catch (e: Exception) {
            Log.e("NetworkFilm", "$e")
        }
    }

    fun openDetail(film: Film) {
        val intent = Intent(this, FilmDetailActivity::class.java)
        intent.putExtra("filmId", film.id)
        startActivity(intent)
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