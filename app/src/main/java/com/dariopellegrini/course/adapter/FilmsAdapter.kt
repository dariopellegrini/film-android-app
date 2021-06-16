package com.dariopellegrini.course.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        holder.configure(films[position])
    }

    override fun getItemCount(): Int {
        return films.size
    }

    class Holder(itemView: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var currentFilm: Film? = null

        fun configure(film: Film) {
            currentFilm = film
            itemView.titleRowTextView.text = film.name
            Glide.with(itemView).load(film.image.medium).into(itemView.imageView)
        }
    }
}