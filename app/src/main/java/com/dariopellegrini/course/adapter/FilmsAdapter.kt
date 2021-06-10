package com.dariopellegrini.course.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dariopellegrini.course.Film
import com.dariopellegrini.course.R
import kotlinx.android.synthetic.main.row_film.view.*

class FilmsAdapter(var films: List<Film>): RecyclerView.Adapter<FilmsAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_film, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val film = films[position]
        holder.configure(film)
    }

    override fun getItemCount(): Int {
        return films.size
    }

    class Holder(val itemView: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var currentFilm: Film? = null
        init {
        }

        fun configure(film: Film) {
            currentFilm = film
            itemView.titleTextView.text = film.name
        }
    }
}