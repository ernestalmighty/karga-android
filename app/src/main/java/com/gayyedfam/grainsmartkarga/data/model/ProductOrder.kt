package com.gayyedfam.grainsmartkarga.data.model

import androidx.room.*
import com.gayyedfam.grainsmartkarga.data.local.converters.Converters

/**
 * Created by emgayyed on 19/7/20.
 */
@Entity
data class ProductOrder(
    @PrimaryKey(autoGenerate = true)
    val productOrderId: Int = 0,
    val productDetailVariantId: String,
    val price: Float,
    @TypeConverters(Converters::class)
    val type: ProductType = ProductType.RICE,
    val category: String = "",
    val variant: String = "",
    val quantity: Int = 0
)