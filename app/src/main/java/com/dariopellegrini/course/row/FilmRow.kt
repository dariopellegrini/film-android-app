package com.dariopellegrini.course.row

import android.view.View
import com.bumptech.glide.Glide
import com.dariopellegrini.course.Film
import com.dariopellegrini.course.R
import com.dariopellegrini.declarativerecycler.Row
import kotlinx.android.synthetic.main.row_film.view.*

class FilmRow(val film: Film, val click: () -> Unit): Row {
    override val layoutID: Int = R.layout.row_film

    override val configuration: ((View, Int) -> Unit) = {
        itemView, position ->

        itemView.titleTextView.text = film.name
        Glide.with(itemView).load(film.image.medium).into(itemView.imageView)

        itemView.setOnClickListener {
            click()
        }
    }
}