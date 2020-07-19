package com.gayyedfam.grainsmartkarga.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Created by emgayyed on 19/7/20.
 */
@Entity
data class ProductOrder(
    @PrimaryKey(autoGenerate = true)
    val productOrderId: Int = 0,
    @ForeignKey(entity = ProductDetailVariant::class,
        parentColumns = ["productDetailVariantId"],
        childColumns = ["productDetailVariantId"],
        onDelete = ForeignKey.CASCADE
    )
    val productDetailVariantId: Int,
    val price: Float
) {
    @Ignore
    val productDetailVariant: ProductDetailVariant ?= null
    @Ignore
    val productDetail: ProductDetail ?= null
    @Ignore
    val product: Product ?= null
}