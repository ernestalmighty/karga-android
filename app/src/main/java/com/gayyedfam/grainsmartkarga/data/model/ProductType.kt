package com.gayyedfam.grainsmartkarga.data.model

/**
 * Created by emgayyed on 18/7/20.
 */
enum class ProductType(val value: Int) {
    WATER(0), RICE(1), GROCERY(2), BILLS_PAYMENT(3), LPG(4);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}