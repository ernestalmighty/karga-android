package com.gayyedfam.grainsmartkarga.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by emgayyed on 2/8/20.
 */
@Entity
data class OrderHistory(
    @PrimaryKey
    val id: String,
    val totalAmount: Float,
    val date: String
)