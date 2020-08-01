package com.gayyedfam.grainsmartkarga.di

import android.content.Context
import androidx.room.Room
import com.gayyedfam.grainsmartkarga.data.local.AppDatabase
import com.gayyedfam.grainsmartkarga.data.local.ProductDAO
import com.gayyedfam.grainsmartkarga.data.local.ProfileDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

/**
 * Created by emgayyed on 18/7/20.
 */
@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context,
            AppDatabase::class.java, "karga-grainsmart.db").build()
    }

    @Provides
    @Singleton
    fun provideProductsDAO(appDatabase: AppDatabase): ProductDAO {
        return appDatabase.productDao()
    }

    @Provides
    @Singleton
    fun provideProfileDAO(appDatabase: AppDatabase): ProfileDAO {
        return appDatabase.profileDao()
    }
}