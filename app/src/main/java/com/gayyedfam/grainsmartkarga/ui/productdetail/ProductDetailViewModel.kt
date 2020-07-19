package com.gayyedfam.grainsmartkarga.ui.productdetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gayyedfam.grainsmartkarga.data.model.ProductDetailVariant
import com.gayyedfam.grainsmartkarga.data.model.ProductOrder
import com.gayyedfam.grainsmartkarga.domain.usecase.GetOrdersUseCase
import com.gayyedfam.grainsmartkarga.domain.usecase.GetProductDetailVariationsUseCase
import com.gayyedfam.grainsmartkarga.domain.usecase.GetProductDetailsUseCase
import com.gayyedfam.grainsmartkarga.domain.usecase.UpdateOrderCartUseCase
import com.gayyedfam.grainsmartkarga.ui.home.OrderBasketState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by emgayyed on 19/7/20.
 */
class ProductDetailViewModel @ViewModelInject constructor(
    val getProductDetailsUseCase: GetProductDetailsUseCase,
    val getProductDetailVariationsUseCase: GetProductDetailVariationsUseCase,
    val updateOrderCartUseCase: UpdateOrderCartUseCase,
    val getOrdersUseCase: GetOrdersUseCase): ViewModel() {

    val productDetailState = MutableLiveData<ProductDetailState>()
    val productDetailVariationState = MutableLiveData<ProductDetailVariationState>()
    val orderBasketState = MutableLiveData<OrderBasketState>()

    private val disposable = CompositeDisposable()

    fun load(productId: Int) {
        disposable.add(
        getProductDetailsUseCase(productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
            .subscribe({
                orderBasketState.value = OrderBasketState.OrdersLoaded(it)
            }, {

            })
        )
    }

    fun loadProductDetailVariations(productDetailId: Int) {
        disposable.add(
        getProductDetailVariationsUseCase(productDetailId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                productDetailVariationState.value = ProductDetailVariationState.ProductDetailVariationLoaded(productDetailId, it)
            }, {
                it.message?.let { message ->
                    productDetailVariationState.value = ProductDetailVariationState.ProductDetailVariationLoadError(message)
                }
            })
        )
    }

    fun updateOrderCart(detailVariant: ProductDetailVariant, isAdded: Boolean) {
        val productOrder = ProductOrder(
            productDetailVariantId = detailVariant.productDetailVariantId,
            price = detailVariant.price
        )

        disposable.add(
        updateOrderCartUseCase(productOrder, isAdded)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loadOrders()
            }, {

            })
        )
    }
}