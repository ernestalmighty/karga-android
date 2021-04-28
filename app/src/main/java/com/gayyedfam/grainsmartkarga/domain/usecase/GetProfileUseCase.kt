package com.gayyedfam.grainsmartkarga.domain.usecase

import com.gayyedfam.grainsmartkarga.data.model.Profile
import com.gayyedfam.grainsmartkarga.data.repository.ProfileRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by emgayyed on 1/8/20.
 */
class GetProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    operator fun invoke(): Single<Profile> {
        return profileRepository.getProfile()
    }
}