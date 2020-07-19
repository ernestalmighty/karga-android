package com.gayyedfam.grainsmartkarga.ui.cart

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gayyedfam.grainsmartkarga.domain.usecase.GetOrdersUseCase
import com.gayyedfam.grainsmartkarga.domain.usecase.GetProductDetailVariationsUseCase
import com.gayyedfam.grainsmartkarga.domain.usecase.GetProductDetailsUseCase
import com.gayyedfam.grainsmartkarga.ui.home.OrderBasketState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by emgayyed on 19/7/20.
 */
class CartViewModel @ViewModelInject constructor(val getOrdersUseCase: GetOrdersUseCase,
                                                 val getProductDetailVariationsUseCase: GetProductDetailVariationsUseCase) : ViewModel() {

    var orderBasketState = MutableLiveData<OrderBasketState>()
    private val disposable = CompositeDisposable()

    fun load() {
        disposable.add(
        getOrdersUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.forEach { productOrder ->
                    getProductDetailVariationsUseCase(productOrder.productDetailVariantId)
                }


                orderBasketState.value = OrderBasketState.OrdersLoaded(it)
            }, {

            })
        )
    }
}