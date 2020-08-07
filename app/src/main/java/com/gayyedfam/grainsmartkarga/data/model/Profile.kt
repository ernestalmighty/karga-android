package com.gayyedfam.grainsmartkarga.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by emgayyed on 1/8/20.
 */
@Entity
data class Profile(
    @PrimaryKey
    val profileId: Int = 0,
    val name: String,
    val contact: String,
    val address: String,
    val deviceId: String = "",
    val deliveryInstruction: String = ""
)