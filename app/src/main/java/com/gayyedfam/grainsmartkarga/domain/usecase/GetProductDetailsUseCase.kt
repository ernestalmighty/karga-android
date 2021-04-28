package com.gayyedfam.grainsmartkarga.domain.usecase

import com.gayyedfam.grainsmartkarga.data.model.ProductDetail
import com.gayyedfam.grainsmartkarga.data.model.ProductDetailWithVariants
import com.gayyedfam.grainsmartkarga.data.model.ProductWithDetail
import com.gayyedfam.grainsmartkarga.data.repository.ProductsRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by emgayyed on 18/7/20.
 */
class GetProductDetailsUseCase @Inject constructor(private val productsRepository: ProductsRepository) {
    operator fun invoke(id: String): Single<List<ProductDetailWithVariants>> {
        return productsRepository.getProductDetails(id)
    }
}