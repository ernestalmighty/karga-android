package com.gayyedfam.grainsmartkarga.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by emgayyed on 3/8/20.
 */
@Entity
data class DeviceLocation (
    @PrimaryKey
    val id: Int = 0,
    val locationLat: Double = 0.0,
    val locationLon: Double = 0.0
)