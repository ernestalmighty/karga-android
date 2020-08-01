package com.gayyedfam.grainsmartkarga.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gayyedfam.grainsmartkarga.data.model.Profile
import io.reactivex.Single

/**
 * Created by emgayyed on 1/8/20.
 */
@Dao
interface ProfileDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(profile: Profile)

    @Query("SELECT * FROM profile")
    fun getProfile(): Single<Profile>
}