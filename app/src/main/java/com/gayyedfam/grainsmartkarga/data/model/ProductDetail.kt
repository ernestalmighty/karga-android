package com.gayyedfam.grainsmartkarga.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by emgayyed on 18/7/20.
 */
@Entity
data class ProductDetail(
    @PrimaryKey
    val productDetailId: String,
    val productIdParent: String,
    val productDetailName: String,
    val productDetailDescription: String = "",
    val productDetailImage: String = ""
)
@Entity
data class ProductDetailVariant(
    @PrimaryKey
    val productDetailVariantId: String,
    val productDetailId: String,
    val productDetailVariantName: String,
    val price: Float,
    val stocksLeft: Int = -1
)