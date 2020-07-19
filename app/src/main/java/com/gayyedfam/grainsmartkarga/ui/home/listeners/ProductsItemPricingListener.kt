package com.gayyedfam.grainsmartkarga.ui.home.listeners

import com.gayyedfam.grainsmartkarga.data.model.ProductDetailVariant

/**
 * Created by emgayyed on 18/7/20.
 */
interface ProductsItemPricingListener {
    fun onProductVariationOrderAdded(productDetailVariant: ProductDetailVariant)
    fun onProductVariationOrderRemoved(productDetailVariant: ProductDetailVariant)
}