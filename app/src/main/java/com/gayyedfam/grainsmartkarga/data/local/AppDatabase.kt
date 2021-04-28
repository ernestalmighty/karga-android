package com.gayyedfam.grainsmartkarga.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gayyedfam.grainsmartkarga.data.local.converters.Converters
import com.gayyedfam.grainsmartkarga.data.model.*

/**
 * Created by emgayyed on 18/7/20.
 */
@Database(
    entities = [
        DeviceLocation::class,
        Profile::class,
        Store::class,
        Product::class,
        ProductDetail::class,
        ProductDetailVariant::class,
        OrderHistory::class,
        ProductOrder::class], version = 3, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDAO
    abstract fun profileDao(): ProfileDAO
}