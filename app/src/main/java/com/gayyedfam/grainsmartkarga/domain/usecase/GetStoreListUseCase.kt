package com.gayyedfam.grainsmartkarga.domain.usecase

import com.gayyedfam.grainsmartkarga.data.model.Store
import com.gayyedfam.grainsmartkarga.data.repository.ProductsRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by emgayyed on 1/8/20.
 */
class GetStoreListUseCase @Inject constructor(val productsRepository: ProductsRepository) {
    operator fun invoke(): Single<List<Store>> {
        return productsRepository.getStoreList()
    }
}