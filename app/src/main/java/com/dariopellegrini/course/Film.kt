package com.dariopellegrini.course

import com.dariopellegrini.storagedone.PrimaryKey

data class Film(val id: Int,
                val name: String,
                val language: String,
                val url: String,
                val runtime: Int,
                val image: Image,
                val genres: List<String>): PrimaryKey {

    var starred = false

    override fun primaryKey(): String {
        return "id"
    }
}