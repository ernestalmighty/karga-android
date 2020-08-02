package com.gayyedfam.grainsmartkarga.ui.home

import com.gayyedfam.grainsmartkarga.data.model.ProductWithDetail

/**
 * Created by emgayyed on 18/7/20.
 */
sealed class HomeState {
    data class ProductsLoaded(val list: List<ProductWithDetail>): HomeState()
    data class ProductLoadingError(val error: String): HomeState()
    data class ProductLoadingProgress(val isLoading: Boolean): HomeState()
}