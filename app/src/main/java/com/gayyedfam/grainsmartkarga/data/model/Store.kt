package com.gayyedfam.grainsmartkarga.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by emgayyed on 1/8/20.
 */
@Entity
data class Store(
    @PrimaryKey
    val storeId: String,
    val name: String,
    val address: String,
    val lat: Double,
    val lon: Double,
    val contact: String ?= "",
    val social: String ?= "",
    val email: String ?= "",
    val messenger: String ?= ""
)