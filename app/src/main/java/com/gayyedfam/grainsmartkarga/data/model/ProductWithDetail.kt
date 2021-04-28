package com.gayyedfam.grainsmartkarga.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

/**
 * Created by emgayyed on 19/7/20.
 */
data class ProductWithDetail(
    @Embedded val product: Product,
    @Relation(
        parentColumn = "productId",
        entityColumn = "productIdParent"
    )
    val productDetails: List<ProductDetail> = listOf()
)