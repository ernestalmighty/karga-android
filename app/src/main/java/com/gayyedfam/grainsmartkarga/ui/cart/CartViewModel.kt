package com.gayyedfam.grainsmartkarga.ui.cart

import androidx.lifecycle.ViewModel
import com.gayyedfam.grainsmartkarga.domain.usecase.GetOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by emgayyed on 19/7/20.
 */
@HiltViewModel
class CartViewModel @Inject constructor(val getOrdersUseCase: GetOrdersUseCase) : ViewModel() {

}