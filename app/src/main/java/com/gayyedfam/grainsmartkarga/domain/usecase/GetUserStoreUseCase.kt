package com.gayyedfam.grainsmartkarga.domain.usecase

import com.gayyedfam.grainsmartkarga.data.model.Store
import com.gayyedfam.grainsmartkarga.data.repository.ProductsRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by emgayyed on 1/8/20.
 */
class GetUserStoreUseCase @Inject constructor(private val productsRepository: ProductsRepository) {
    operator fun invoke(): Single<Store> {
        return productsRepository.getStore()
    }
}