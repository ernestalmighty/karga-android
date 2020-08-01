package com.gayyedfam.grainsmartkarga.data.repository

import com.gayyedfam.grainsmartkarga.data.local.ProfileDAO
import com.gayyedfam.grainsmartkarga.data.model.Profile
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by emgayyed on 1/8/20.
 */
class ProfileRepository @Inject constructor(private val profileDAO: ProfileDAO) {

    fun getProfile(): Single<Profile> {
        return profileDAO.getProfile()
    }

    fun saveProfile(profile: Profile): Single<Boolean> {
        return Single.create<Boolean> {
            profileDAO.insert(profile)
        }
    }
}