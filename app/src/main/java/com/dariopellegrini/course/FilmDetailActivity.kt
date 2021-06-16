package com.dariopellegrini.course

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.dariopellegrini.spike.mapping.mappingThrowable
import com.dariopellegrini.spike.mapping.suspend
import com.dariopellegrini.spike.request
import com.dariopellegrini.storagedone.get
import com.dariopellegrini.storagedone.insertOrUpdate
import com.dariopellegrini.storagedone.query.and
import com.dariopellegrini.storagedone.query.equal
import com.dariopellegrini.storagedone.suspending
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class FilmDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        load(intent.getIntExtra("filmId", -1))
    }

    private fun load(id: Int) = CoroutineScope(Dispatchers.Main).launch {
        if (id == -1) return@launch

        try {
            val film = request<Film> {
                baseURL = " https://api.tvmaze.com/"
                path = "shows/$id"
            }.suspend.mappingThrowable()

            titleTextView.text = film.name
            languageTextView.text = film.language
            durationTextView.text = "Runtime ${film.runtime}"
            genresTextView.text = film.genres
                .filter { it.length > 5 }
                .map { "$it (${it.length})" }
                .joinToString(" - ")

            Glide.with(this@FilmDetailActivity)
                .load(film.image.original)
                .into(imageView)

            websiteButton.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(film.url))
                startActivity(browserIntent)
            }

            // Star
            film.starred = isStarred(id)


            favouriteButton.text = if (film.starred) "Starred" else "Star"

            favouriteButton.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    film.starred = !film.starred
                    DatabaseRepository.database.suspending.insertOrUpdate(film)
                    favouriteButton.text = if (film.starred) "Starred" else "Star"
                }
            }

        } catch (e: Exception) {

        }
    }

    private suspend fun isStarred(id: Int): Boolean {
        val favourites = DatabaseRepository.database.suspending.get<Film>(and("id" equal id, "starred" equal true))
        return favourites.isNotEmpty()
    }
}