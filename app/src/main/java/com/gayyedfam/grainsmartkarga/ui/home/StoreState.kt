package com.gayyedfam.grainsmartkarga.ui.home

import com.gayyedfam.grainsmartkarga.data.model.Store

/**
 * Created by emgayyed on 2/8/20.
 */
sealed class StoreState {
    data class StoresLoaded(val list: List<Store>): StoreState()
    data class UserStoreLoaded(val store: Store): StoreState()
    object Nothing: StoreState()
}