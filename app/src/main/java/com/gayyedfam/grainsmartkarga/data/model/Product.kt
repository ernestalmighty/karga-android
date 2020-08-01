package com.gayyedfam.grainsmartkarga.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.gayyedfam.grainsmartkarga.data.local.converters.Converters

/**
 * Created by emgayyed on 17/7/20.
 */
@Entity
data class Product(
    @PrimaryKey
    val productId: String,
    val name: String,
    val iconUrl: String,
    val status: Boolean,
    @TypeConverters(Converters::class)
    val productType: ProductType
)