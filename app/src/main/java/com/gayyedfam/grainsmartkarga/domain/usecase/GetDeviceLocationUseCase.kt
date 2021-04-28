package com.gayyedfam.grainsmartkarga.domain.usecase

import com.gayyedfam.grainsmartkarga.data.model.DeviceLocation
import com.gayyedfam.grainsmartkarga.data.repository.ProfileRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by emgayyed on 3/8/20.
 */
class GetDeviceLocationUseCase @Inject constructor(val profileRepository: ProfileRepository) {
    operator fun invoke(): Single<DeviceLocation> {
        return profileRepository.getDeviceLocation()
    }
}