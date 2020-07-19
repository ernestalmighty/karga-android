package com.gayyedfam.grainsmartkarga.ui.home

import com.gayyedfam.grainsmartkarga.data.model.Product

/**
 * Created by emgayyed on 18/7/20.
 */
sealed class HomeState {
    data class ProductsLoaded(val list: List<Product>): HomeState()
    data class ProductLoadingError(val error: String): HomeState()
}