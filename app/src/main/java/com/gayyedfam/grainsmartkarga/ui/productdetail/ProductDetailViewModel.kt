package com.gayyedfam.grainsmartkarga.ui.productdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gayyedfam.grainsmartkarga.data.model.ProductDetailVariant
import com.gayyedfam.grainsmartkarga.data.model.ProductOrder
import com.gayyedfam.grainsmartkarga.data.model.ProductType
import com.gayyedfam.grainsmartkarga.domain.usecase.GetOrdersUseCase
import com.gayyedfam.grainsmartkarga.domain.usecase.GetProductDetailsUseCase
import com.gayyedfam.grainsmartkarga.domain.usecase.UpdateOrderCartUseCase
import com.gayyedfam.grainsmartkarga.ui.home.OrderBasketState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by emgayyed on 19/7/20.
 */
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    val getProductDetailsUseCase: GetProductDetailsUseCase,
    val updateOrderCartUseCase: UpdateOrderCartUseCase,
    val getOrdersUseCase: GetOrdersUseCase): ViewModel() {

    val productDetailState = MutableLiveData<ProductDetailState>()
    val orderBasketState = MutableLiveData<OrderBasketState>()

    private val disposable = CompositeDisposable()
    private var initialOrderCount: Int = 0

    fun load(productId: String) {
        disposable.add(
        getProductDetailsUseCase(productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                productDetailState.value = ProductDetailState.ProductDetailLoading(true)
            }
            .doFinally {
                productDetailState.value = ProductDetailState.ProductDetailLoading(false)
            }
            .subscribe({
                productDetailState.value = ProductDetailState.ProductDetailLoaded(it)
            }, {
                it.message?.let { message ->
                    productDetailState.value = ProductDetailState.ProductDetailLoadError(message)
                }
            })
        )
    }

    fun loadOrders() {
        disposable.add(
        getOrdersUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                orderBasketState.value = OrderBasketState.OrderUpdated(initialOrderCount)
            }
            .subscribe({
                initialOrderCount = it.size
                orderBasketState.value = OrderBasketState.OrdersBasketLoaded(it)
            }, {

            })
        )
    }

    fun updateOrderCart(isAdded: Boolean,
                        detailVariant: ProductDetailVariant,
                        productType: ProductType = ProductType.RICE,
                        categoryName: String = "",
                        image: String = "") {



        val productOrder = ProductOrder(
            productDetailVariantId = detailVariant.productDetailVariantId,
            price = detailVariant.price,
            type = productType,
            category = categoryName,
            variant = detailVariant.productDetailVariantName,
            image = image
        )

        if(isAdded) {
            initialOrderCount++
        } else {
            initialOrderCount--
        }

        disposable.add(
        updateOrderCartUseCase(productOrder, isAdded)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                orderBasketState.value = OrderBasketState.OrderUpdated(initialOrderCount)
                //loadOrders()
            }
            .subscribe()
        )
    }
}