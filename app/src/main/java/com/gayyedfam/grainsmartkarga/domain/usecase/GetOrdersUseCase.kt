package com.gayyedfam.grainsmartkarga.domain.usecase

import com.gayyedfam.grainsmartkarga.data.model.ProductOrder
import com.gayyedfam.grainsmartkarga.data.repository.ProductsRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by emgayyed on 19/7/20.
 */
class GetOrdersUseCase @Inject constructor(val productsRepository: ProductsRepository) {
    operator fun invoke(): Single<List<ProductOrder>> {
        return productsRepository.getOrders()
    }

    operator fun invoke(detailVariantId: Int): Single<List<ProductOrder>> {
        return productsRepository.getOrders(detailVariantId)
    }
}