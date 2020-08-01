package com.gayyedfam.grainsmartkarga.domain.usecase

import com.gayyedfam.grainsmartkarga.data.model.ProductWithDetail
import com.gayyedfam.grainsmartkarga.data.repository.ProductsRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by emgayyed on 18/7/20.
 */
class GetProductsUseCase @Inject constructor(private val productsRepository: ProductsRepository) {
    operator fun invoke(): Single<List<ProductWithDetail>> {
        return productsRepository.getProducts()
    }
}