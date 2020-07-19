package com.gayyedfam.grainsmartkarga.domain.usecase

import com.gayyedfam.grainsmartkarga.data.repository.ProductsRepository
import javax.inject.Inject

/**
 * Created by emgayyed on 18/7/20.
 */
class SetupDataUseCase @Inject constructor(private val productsRepository: ProductsRepository) {
    operator fun invoke() {
        productsRepository.load()
    }
}