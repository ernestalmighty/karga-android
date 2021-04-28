package com.gayyedfam.grainsmartkarga.domain.usecase

import com.gayyedfam.grainsmartkarga.data.model.Store
import com.gayyedfam.grainsmartkarga.data.repository.ProductsRepository
import com.gayyedfam.grainsmartkarga.data.repository.ProfileRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by emgayyed on 18/7/20.
 */
class SetupDataUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    operator fun invoke(lat: Double, lon: Double): Single<Boolean> {
        return profileRepository.saveLocation(lat, lon)
    }
}