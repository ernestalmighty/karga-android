package com.gayyedfam.grainsmartkarga.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

/**
 * Created by emgayyed on 19/7/20.
 */
data class ProductDetailWithVariants(
    @Embedded val productDetail: ProductDetail,
    @Relation(
        parentColumn = "productDetailId",
        entityColumn = "productDetailId"
    )
    var variants: List<ProductDetailVariant> = listOf()
)