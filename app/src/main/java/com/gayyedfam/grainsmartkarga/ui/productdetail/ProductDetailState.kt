package com.gayyedfam.grainsmartkarga.ui.productdetail

import com.gayyedfam.grainsmartkarga.data.model.ProductDetailWithVariants

/**
 * Created by emgayyed on 19/7/20.
 */
sealed class ProductDetailState {
    data class ProductDetailLoading(val loading: Boolean): ProductDetailState()
    data class ProductDetailLoaded(val productDetails: List<ProductDetailWithVariants>) : ProductDetailState()
    data class ProductDetailLoadError(val error: String) : ProductDetailState()
}