package com.gayyedfam.grainsmartkarga.ui.orderlist

import android.location.Location
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gayyedfam.grainsmartkarga.data.model.OrderGroup
import com.gayyedfam.grainsmartkarga.data.model.ProductOrder
import com.gayyedfam.grainsmartkarga.data.model.Profile
import com.gayyedfam.grainsmartkarga.domain.usecase.*
import com.gayyedfam.grainsmartkarga.ui.home.OrderBasketState
import com.gayyedfam.grainsmartkarga.ui.profile.ProfileViewState
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.math.roundToInt

/**
 * Created by emgayyed on 24/7/20.
 */
class OrderListViewModel @ViewModelInject constructor(val getOrdersUseCase: GetOrdersUseCase,
                                                      val getProfileUseCase: GetProfileUseCase,
                                                      val saveOrdersUseCase: SaveOrdersUseCase,
                                                      val getOrderHistoryUseCase: GetOrderHistoryUseCase,
                                                      val getUserStoreUseCase: GetUserStoreUseCase,
                                                      val getAdditionalFeeUseCase: GetAdditionalFeeUseCase,
                                                      val deleteOrdersUseCase: DeleteOrdersUseCase,
                                                      val getStoreUseDistanceUseCase: GetStoreUseDistanceUseCase) : ViewModel() {

    var orderBasketState = MutableLiveData<OrderBasketState>()
    var profileState = MutableLiveData<ProfileViewState>()
    private val disposable = CompositeDisposable()
    private var profile: Profile ?= null
    private var orderGroup: List<OrderGroup> = listOf()

    fun load() {
        disposable.add(
            getOrdersUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    if(it.isEmpty()) {
                        orderBasketState.value = OrderBasketState.OrdersEmpty
                    } else {
                        orderBasketState.value = OrderBasketState.OrdersLoaded(it)
                    }

                }
                .subscribe()
        )
    }

    fun getProfile() {
        disposable.add(
        getProfileUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                profile = it
                profileState.value = ProfileViewState.ProfileLoaded(it)
            }, {

            }))
    }

    fun orderSummary(orderList: List<ProductOrder>) {
        disposable.add(
        getAdditionalFeeUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { deliveryFee ->

                    getStoreUseDistanceUseCase()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { distance ->
                                val kilometerDistance = (distance / 1000).roundToInt()
                                var additionalFee = 0
                                if(deliveryFee.isEnabled) {
                                    if(kilometerDistance > deliveryFee.freeRadius) {
                                        val extraDistance = (kilometerDistance - deliveryFee.freeRadius)
                                        additionalFee = extraDistance * deliveryFee.deliveryExtra
                                    }
                                }

                                val totalAmount = orderList.map { order ->
                                    order.price
                                }.sum()

                                val result = orderList.groupBy { order ->
                                    order.productDetailVariantId
                                }

                                val orderGroups = mutableListOf<OrderGroup>()
                                result.entries.map {(_, group) ->
                                    val orderGroup = OrderGroup(
                                        group[0].productDetailVariantId,
                                        group
                                    )
                                    orderGroups.add(orderGroup)
                                }

                                this.orderGroup = orderGroups
                                orderBasketState.value = OrderBasketState.OrdersSummarized((totalAmount + additionalFee).toString(), additionalFee.toString(), orderGroups)
                            },
                            {
                                orderBasketState.value = OrderBasketState.Nothing
                            }
                        )
                },
                {
                    orderBasketState.value = OrderBasketState.Nothing
                }
            )
        )
    }

    private fun removeOrders() {
        disposable.add(
        deleteOrdersUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    load()
                },
                {

                }
            ))
    }

    fun orderFollowUp() {
        disposable.add(
            getUserStoreUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        it.messenger?.let { link ->
                            orderBasketState.value = OrderBasketState.OrderFollowUp(link)
                        }
                    },
                    {

                    }
                )

        )
    }

    fun orderConfirmation() {
        profile?.let {
            saveOrdersUseCase(it, orderGroup)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    orderBasketState.value = OrderBasketState.OrderLoading(true)
                }
                .doFinally {
                    orderBasketState.value = OrderBasketState.OrderLoading(false)
                }
                .subscribe(
                    { referenceId ->
                        removeOrders()
                        orderBasketState.value = OrderBasketState.OrderSuccessful(referenceId)
                    },
                    {
                        orderBasketState.value = OrderBasketState.OrderError("An error occurred unexpectedly. Please try again.")
                    }
                )
        } ?: {
            orderBasketState.value = OrderBasketState.OrderError("Your delivery address is not set.")
            orderBasketState.value = OrderBasketState.Nothing
        }()
    }

    fun orderHistory() {
        disposable.add(
            getOrderHistoryUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    orderBasketState.value = OrderBasketState.Nothing
                }
                .subscribe(
                    {
                        if(it.isEmpty()) {
                            orderBasketState.value = OrderBasketState.OrderHistoryEmpty
                        } else {
                            orderBasketState.value = OrderBasketState.OrderHistoryLoaded(it)
                        }
                    },
                    {}
                )
        )
    }
}