package com.gayyedfam.grainsmartkarga.domain.usecase

import com.gayyedfam.grainsmartkarga.data.model.ProductOrder
import com.gayyedfam.grainsmartkarga.data.repository.ProductsRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by emgayyed on 19/7/20.
 */
class UpdateOrderCartUseCase @Inject constructor(private val productsRepository: ProductsRepository) {
    operator fun invoke(productOrder: ProductOrder, addOrder: Boolean): Single<Boolean> {
        return if(addOrder) {
            productsRepository.addToOrderBasket(productOrder)
        } else {
            productsRepository.removeFromOrderBasket(productOrder)
        }
    }
}