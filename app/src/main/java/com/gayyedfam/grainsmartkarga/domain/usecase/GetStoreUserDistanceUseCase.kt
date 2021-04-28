package com.gayyedfam.grainsmartkarga.domain.usecase

import com.gayyedfam.grainsmartkarga.data.repository.ProductsRepository
import com.gayyedfam.grainsmartkarga.data.repository.ProfileRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by emgayyed on 10/8/20.
 */
class GetStoreUserDistanceUseCase @Inject constructor(val productsRepository: ProductsRepository) {
    operator fun invoke(): Single<Double> {
        return productsRepository.getDistanceDeviceStore()
    }
}