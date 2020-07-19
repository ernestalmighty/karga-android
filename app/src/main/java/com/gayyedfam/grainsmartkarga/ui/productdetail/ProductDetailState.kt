package com.gayyedfam.grainsmartkarga.ui.productdetail

import com.gayyedfam.grainsmartkarga.data.model.ProductDetail

/**
 * Created by emgayyed on 19/7/20.
 */
sealed class ProductDetailState {
    data class ProductDetailLoaded(val productDetails: List<ProductDetail>) : ProductDetailState()
    data class ProductDetailLoadError(val error: String) : ProductDetailState()
}