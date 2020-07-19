package com.gayyedfam.grainsmartkarga.ui.home

import com.gayyedfam.grainsmartkarga.data.model.ProductOrder

/**
 * Created by emgayyed on 19/7/20.
 */
sealed class OrderBasketState {
    data class OrdersLoaded(val list: List<ProductOrder>): OrderBasketState()
    data class OrdersLoadError(val error: String): OrderBasketState()
}