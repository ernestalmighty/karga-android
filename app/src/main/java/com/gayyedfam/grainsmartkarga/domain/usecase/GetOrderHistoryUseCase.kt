package com.gayyedfam.grainsmartkarga.domain.usecase

import com.gayyedfam.grainsmartkarga.data.model.OrderHistory
import com.gayyedfam.grainsmartkarga.data.repository.ProductsRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by emgayyed on 2/8/20.
 */
class GetOrderHistoryUseCase @Inject constructor(val productsRepository: ProductsRepository) {
    operator fun invoke(): Single<List<OrderHistory>> {
        return productsRepository.getOrderHistory()
    }
}