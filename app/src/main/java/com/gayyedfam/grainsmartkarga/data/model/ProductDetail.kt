package com.gayyedfam.grainsmartkarga.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Created by emgayyed on 18/7/20.
 */
@Entity
data class ProductDetail(
    @PrimaryKey
    val productDetailId: Int,
    @ForeignKey(entity = Product::class,
        parentColumns = ["productId"],
        childColumns = ["productId"],
        onDelete = CASCADE)
    val productId: Int,
    val productDetailName: String,
    val productDetailDescription: String = ""
) {
    @Ignore
    var variants: List<ProductDetailVariant> = listOf()
}

@Entity
data class ProductDetailVariant(
    @PrimaryKey
    val productDetailVariantId: Int,
    @ForeignKey(entity = ProductDetail::class,
        parentColumns = ["productDetailId"],
        childColumns = ["productDetailId"],
        onDelete = CASCADE)
    val productDetailId: Int,
    val productDetailVariantName: String,
    val price: Float
) {
    @Ignore
    var orders: List<ProductOrder> = listOf()
}