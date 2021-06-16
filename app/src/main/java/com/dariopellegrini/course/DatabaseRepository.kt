package com.dariopellegrini.course

import com.dariopellegrini.storagedone.StorageDoneDatabase

object DatabaseRepository {
    val database: StorageDoneDatabase by lazy {
        StorageDoneDatabase("films")
    }
}