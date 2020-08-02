package com.gayyedfam.grainsmartkarga.ui.home

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.EmptyResultSetException
import com.gayyedfam.grainsmartkarga.data.model.Store
import com.gayyedfam.grainsmartkarga.domain.usecase.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by emgayyed on 18/7/20.
 */
class HomeViewModel @ViewModelInject constructor(
    val getProductsUseCase: GetProductsUseCase,
    val getOrdersUseCase: GetOrdersUseCase,
    val getUserStoreUseCase: GetUserStoreUseCase,
    val getStoreListUseCase: GetStoreListUseCase,
    val deleteOrdersUseCase: DeleteOrdersUseCase,
    val saveStoreUseCase: SaveStoreUseCase): ViewModel() {

    val homeStateLiveData = MutableLiveData<HomeState>()
    val basketStateLiveData = MutableLiveData<OrderBasketState>()
    val storeStateLiveData = MutableLiveData<StoreState>()
    private val disposable = CompositeDisposable()
    private var userStore: Store ?= null

    init {
        loadUserStore()
    }

    private fun loadUserStore() {
        disposable.add(
            getUserStoreUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        storeStateLiveData.value = StoreState.UserStoreLoaded(it)
                        storeSelected(it)
                    },
                    {
                        if(it is EmptyResultSetException) {
                            getStoreListUseCase()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    { stores ->
                                        storeStateLiveData.value = StoreState.UserStoreLoaded(stores.first())
                                        storeSelected(stores.first())
                                    },
                                    {

                                    }
                                )
                        } else {
                            Log.d("TAG", "TAG")
                        }
                    }
                ))
    }

    fun onResume() {
        userStore?.let {
            storeStateLiveData.value = StoreState.UserStoreLoaded(it)
            load(it.storeId)
        }

        loadOrders()
    }

    fun loadStore() {
        disposable.add(
        getStoreListUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                storeStateLiveData.value = StoreState.Nothing
            }
            .subscribe(
                {
                    storeStateLiveData.value = StoreState.StoresLoaded(it)
                },
                {

                }
            ))
    }

    fun clearOrders() {
        disposable.add(
        deleteOrdersUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    loadOrders()
                },
                {}
            ))
    }

    fun load(storeId: String) {
        disposable.add(
        getProductsUseCase(storeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                homeStateLiveData.value = HomeState.ProductLoadingProgress(true)
            }
            .doFinally {
                homeStateLiveData.value = HomeState.ProductLoadingProgress(false)
            }
            .subscribe({
                homeStateLiveData.value = HomeState.ProductsLoaded(it)
            }, {
                it.message?.let { message ->
                    homeStateLiveData.value = HomeState.ProductLoadingError(message)
                }
            })
        )
    }

    fun loadOrders() {
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

    fun storeSelected(store: Store) {
        userStore = store
        disposable.add(
        saveStoreUseCase(store)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    load(store.storeId)
                },
                {

                }
            ))
    }
}