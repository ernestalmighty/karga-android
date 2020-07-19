package com.gayyedfam.grainsmartkarga.domain.usecase

import com.gayyedfam.grainsmartkarga.data.model.ProductDetailVariant
import com.gayyedfam.grainsmartkarga.data.repository.ProductsRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by emgayyed on 19/7/20.
 */
class GetProductDetailVariationsUseCase @Inject constructor(private val productsRepository: ProductsRepository) {
    operator fun invoke(productDetailId: Int): Single<List<ProductDetailVariant>> {
        return productsRepository.getProductDetailVariants(productDetailId)
    }
}