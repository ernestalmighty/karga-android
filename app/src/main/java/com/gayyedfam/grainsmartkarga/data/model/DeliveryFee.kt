package com.gayyedfam.grainsmartkarga.data.model

/**
 * Created by emgayyed on 10/8/20.
 */
data class DeliveryFee(
    var displayName: String = "",
    var isEnabled: Boolean = false,
    var freeRadius: Int = 10,
    var deliveryExtra: Int = 0
)