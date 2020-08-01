package com.gayyedfam.grainsmartkarga.domain.usecase

import com.gayyedfam.grainsmartkarga.data.model.OrderGroup
import com.gayyedfam.grainsmartkarga.data.model.Profile
import com.gayyedfam.grainsmartkarga.data.repository.ProductsRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by emgayyed on 1/8/20.
 */
class SaveOrdersUseCase @Inject constructor(private val productsRepository: ProductsRepository) {
    operator fun invoke(profile: Profile, list: List<OrderGroup>): Single<Boolean> {
        return productsRepository.postOrders(profile, list)
    }
}