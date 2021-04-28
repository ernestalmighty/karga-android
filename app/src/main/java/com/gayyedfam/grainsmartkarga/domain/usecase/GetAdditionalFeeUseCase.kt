package com.gayyedfam.grainsmartkarga.domain.usecase

import com.gayyedfam.grainsmartkarga.data.model.DeliveryFee
import com.gayyedfam.grainsmartkarga.data.repository.ProductsRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by emgayyed on 10/8/20.
 */
class GetAdditionalFeeUseCase @Inject constructor(val productsRepository: ProductsRepository) {
    operator fun invoke(): Single<DeliveryFee> {
        return productsRepository.getDeliveryFee()
    }
}