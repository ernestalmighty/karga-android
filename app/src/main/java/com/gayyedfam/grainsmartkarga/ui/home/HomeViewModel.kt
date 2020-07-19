package com.gayyedfam.grainsmartkarga.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gayyedfam.grainsmartkarga.domain.usecase.GetOrdersUseCase
import com.gayyedfam.grainsmartkarga.domain.usecase.GetProductsUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by emgayyed on 18/7/20.
 */
class HomeViewModel @ViewModelInject constructor(
    val getProductsUseCase: GetProductsUseCase,
    val getOrdersUseCase: GetOrdersUseCase): ViewModel() {

    val homeStateLiveData = MutableLiveData<HomeState>()
    val basketStateLiveData = MutableLiveData<OrderBasketState>()
    private val disposable = CompositeDisposable()

    fun load() {
        disposable.add(
        getProductsUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                homeStateLiveData.value = HomeState.ProductsLoaded(it)
            }, {
                it.message?.let { message ->
                    homeStateLiveData.value = HomeState.ProductLoadingError(message)
                }
            })
        )

        disposable.add(
        getOrdersUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                basketStateLiveData.value = OrderBasketState.OrdersLoaded(it)
            }, {
                it.message?.let { message ->
                    basketStateLiveData.value = OrderBasketState.OrdersLoadError(message)
                }
            })
        )
    }
}