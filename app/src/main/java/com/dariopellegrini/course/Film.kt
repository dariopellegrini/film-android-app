package com.dariopellegrini.course

data class Film(val name: String,
                val language: String,
                val url: String,
                val runtime: Int,
                val image: Image,
                val genres: List<String>)