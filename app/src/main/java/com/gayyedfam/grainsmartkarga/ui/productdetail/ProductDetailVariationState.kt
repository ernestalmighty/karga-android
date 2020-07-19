package com.gayyedfam.grainsmartkarga.ui.productdetail

import com.gayyedfam.grainsmartkarga.data.model.ProductDetailVariant
import com.gayyedfam.grainsmartkarga.data.model.ProductOrder

/**
 * Created by emgayyed on 19/7/20.
 */
sealed class ProductDetailVariationState {
    data class ProductOrdersLoaded(val list: List<ProductOrder>) : ProductDetailVariationState()
    data class ProductDetailVariationLoaded(val productDetailId: Int, val list: List<ProductDetailVariant>) : ProductDetailVariationState()
    data class ProductDetailVariationLoadError(val error: String) : ProductDetailVariationState()
}