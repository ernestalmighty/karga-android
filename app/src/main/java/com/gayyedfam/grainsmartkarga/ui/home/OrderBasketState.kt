package com.gayyedfam.grainsmartkarga.ui.home

import com.gayyedfam.grainsmartkarga.data.model.OrderGroup
import com.gayyedfam.grainsmartkarga.data.model.OrderHistory
import com.gayyedfam.grainsmartkarga.data.model.ProductOrder

/**
 * Created by emgayyed on 19/7/20.
 */
sealed class OrderBasketState {
    data class OrdersLoaded(val list: List<OrderGroup>): OrderBasketState()
    data class OrdersBasketLoaded(val list: List<ProductOrder>): OrderBasketState()
    data class OrdersSummarized(val totalAmount: String, val deliveryFee: String): OrderBasketState()
    data class OrdersLoadError(val error: String): OrderBasketState()
    data class OrderLoading(val loading: Boolean): OrderBasketState()
    data class OrderSuccessful(val referenceId: String): OrderBasketState()
    data class OrderError(val message: String): OrderBasketState()
    data class OrderHistoryLoaded(val list: List<OrderHistory>): OrderBasketState()
    data class OrderFollowUp(val messengerLink: String): OrderBasketState()
    object NoMessengerLink: OrderBasketState()
    object OrderHistoryEmpty: OrderBasketState()
    object OrdersEmpty: OrderBasketState()
    data class OrderUpdated(val count: Int): OrderBasketState()
    object Nothing: OrderBasketState()
}