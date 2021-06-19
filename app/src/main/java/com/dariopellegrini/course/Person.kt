package com.dariopellegrini.course

import com.dariopellegrini.storagedone.PrimaryKey

data class Person(val id: Int,
                  val name: String,
                  val surname: String): PrimaryKey {
    override fun primaryKey(): String = "id"
}

data class Configuration(val name: String)