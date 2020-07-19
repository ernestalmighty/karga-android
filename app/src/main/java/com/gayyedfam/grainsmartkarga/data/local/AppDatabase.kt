package com.gayyedfam.grainsmartkarga.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gayyedfam.grainsmartkarga.data.local.converters.Converters
import com.gayyedfam.grainsmartkarga.data.model.Product
import com.gayyedfam.grainsmartkarga.data.model.ProductDetail
import com.gayyedfam.grainsmartkarga.data.model.ProductDetailVariant
import com.gayyedfam.grainsmartkarga.data.model.ProductOrder

/**
 * Created by emgayyed on 18/7/20.
 */
@Database(entities = [Product::class, ProductDetail::class, ProductDetailVariant::class, ProductOrder::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDAO
}