package com.gayyedfam.grainsmartkarga.data.local.converters

import androidx.room.TypeConverter
import com.gayyedfam.grainsmartkarga.data.model.ProductType

/**
 * Created by emgayyed on 18/7/20.
 */
class Converters {
    @TypeConverter
    fun from(value: Int): ProductType {
        return ProductType.values()[value]
    }

    @TypeConverter
    fun to(productType: ProductType): Int {
        return productType.ordinal
    }
}