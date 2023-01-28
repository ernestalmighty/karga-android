package com.gayyedfam.grainsmartkarga.ui.home

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.EmptyResultSetException
import com.gayyedfam.grainsmartkarga.data.model.DeviceLocation
import com.gayyedfam.grainsmartkarga.data.model.Store
import com.gayyedfam.grainsmartkarga.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by emgayyed on 18/7/20.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    val getProductsUseCase: GetProductsUseCase,
    val getOrdersUseCase: GetOrdersUseCase,
    val getUserStoreUseCase: GetUserStoreUseCase,
    val getStoreListUseCase: GetStoreListUseCase,
    val deleteOrdersUseCase: DeleteOrdersUseCase,
    val getDeviceLocationUseCase: GetDeviceLocationUseCase,
    val saveStoreUseCase: SaveStoreUseCase): ViewModel() {

    val homeStateLiveData = MutableLiveData<HomeState>()
    val basketStateLiveData = MutableLiveData<OrderBasketState>()
    val storeStateLiveData = MutableLiveData<StoreState>()
    private val disposable = CompositeDisposable()
    private var userStore: Store ?= null
    private var deviceLocation: DeviceLocation ?= null

    init {
        loadDeviceLocation()
    }

    private fun loadDeviceLocation() {
        disposable.add(
            getDeviceLocationUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    loadUserStore()
                }
                .subscribe(
                    {
                        this.deviceLocation = it
                    },
                    {

                    }
                )
        )
    }

    fun loadUserStore() {
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
                            storeStateLiveData.value = StoreState.UserStoreEmpty
                        } else {
                            Log.d("TAG", "TAG")
                        }
                    }
                ))
    }

    fun emptyUserStore() {
        disposable.add(
        getStoreListUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { stores ->
                    if(stores.isEmpty()) {
                        storeStateLiveData.value = StoreState.StoreListEmpty
                    } else {
                        deviceLocation?.let {
                            val userStore = stores.minBy { store ->
                                calculateDistance(store.lat, store.lon)
                            }

                            userStore?.let { newStore ->
                                storeStateLiveData.value = StoreState.UserStoreLoaded(newStore)
                                storeSelected(newStore)
                            } ?: {
                                storeStateLiveData.value = StoreState.UserStoreLoaded(stores.first())
                                storeSelected(stores.first())
                            }()
                        } ?: {
                            storeStateLiveData.value = StoreState.UserStoreLoaded(stores.first())
                            storeSelected(stores.first())
                        }()
                    }
                },
                {

                }
            ))
    }

    private fun calculateDistance(lat: Double, lon: Double): Float {
        var distance = 0F
        deviceLocation?.let {
            val location = Location("store")
            location.latitude = lat
            location.longitude = lon

            val currentLocation = Location("device")
            currentLocation.latitude = it.locationLat
            currentLocation.longitude = it.locationLon

            distance = currentLocation.distanceTo(location)
        }

        return distance
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
                    if(it.isEmpty()) {
                        storeStateLiveData.value = StoreState.StoreListEmpty
                    } else {
                        storeStateLiveData.value = StoreState.StoresLoaded(it)
                    }
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
                    basketStateLiveData.value = OrderBasketState.OrdersBasketLoaded(it)
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
            .doFinally {
                storeStateLiveData.value = StoreState.Nothing
            }
            .subscribe(
                {
                    storeStateLiveData.value = StoreState.UserStoreSelected(store.storeId)
                },
                {

                }
            ))
    }
}