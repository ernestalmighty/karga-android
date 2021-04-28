package com.gayyedfam.grainsmartkarga.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

/**
 * Created by emgayyed on 19/7/20.
 */
data class ProductVariantsWithOrders(
    @Embedded val productDetailVariant: ProductDetailVariant,
    @Relation(
        parentColumn = "productDetailVariantId",
        entityColumn = "productDetailVariantId"
    )
    val variants: List<ProductOrder>
)