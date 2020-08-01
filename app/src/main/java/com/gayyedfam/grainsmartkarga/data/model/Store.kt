package com.gayyedfam.grainsmartkarga.data.model

import androidx.room.PrimaryKey

/**
 * Created by emgayyed on 1/8/20.
 */
data class Store(
    @PrimaryKey
    val storeId: String,
    val name: String,
    val address: String
)