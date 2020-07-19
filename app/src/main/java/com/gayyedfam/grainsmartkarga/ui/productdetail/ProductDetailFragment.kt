package com.gayyedfam.grainsmartkarga.ui.productdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gayyedfam.grainsmartkarga.R
import com.gayyedfam.grainsmartkarga.data.model.ProductDetail
import com.gayyedfam.grainsmartkarga.data.model.ProductDetailVariant
import com.gayyedfam.grainsmartkarga.ui.home.OrderBasketState
import com.gayyedfam.grainsmartkarga.ui.home.listeners.ProductsItemPricingListener
import com.gayyedfam.grainsmartkarga.ui.productdetail.ProductDetailFragmentArgs.Companion.fromBundle
import com.gayyedfam.grainsmartkarga.ui.productdetail.adapters.ProductsDetailListAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_product_detail.*

@AndroidEntryPoint
class ProductDetailFragment : Fragment(), ProductsItemPricingListener {

    private val productId by lazy {
        fromBundle(requireArguments()).productId
    }

    private val productName by lazy {
        fromBundle(requireArguments()).productName
    }

    private val productDetailViewModel: ProductDetailViewModel by viewModels()
    private val productDetailAdapter = ProductsDetailListAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onResume() {
        super.onResume()
        setToolbar()

        productDetailViewModel.productDetailState.observeForever {
            when(it) {
                is ProductDetailState.ProductDetailLoaded -> {
                    it.productDetails.forEach { detail ->
                        productDetailViewModel.loadProductDetailVariations(detail.productDetailId)
                    }

                    setupList(it.productDetails)
                }
                is ProductDetailState.ProductDetailLoadError -> {

                }
            }
        }

        productDetailViewModel.productDetailVariationState.observeForever {
            when(it) {
                is ProductDetailVariationState.ProductDetailVariationLoaded -> {
                    productDetailAdapter.updateProductDetail(it.productDetailId, it.list)
                }
                is ProductDetailVariationState.ProductDetailVariationLoadError -> {

                }
            }
        }

        productDetailViewModel.orderBasketState.observeForever {
            when(it) {
                is OrderBasketState.OrdersLoaded -> {
                    productDetailAdapter.ordersList = it.list
                }
            }
        }

        productDetailViewModel.load(productId)
        productDetailViewModel.loadOrders()
    }

    private fun setToolbar() {
        textViewToolbarTitle.text = productName
        imageViewBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupList(list: List<ProductDetail>) {
        recyclerViewProductDetails.adapter = productDetailAdapter
        recyclerViewProductDetails.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        productDetailAdapter.list = list

        productDetailAdapter.notifyDataSetChanged()
    }

    override fun onProductVariationOrderAdded(productDetailVariant: ProductDetailVariant) {
        productDetailViewModel.updateOrderCart(productDetailVariant, true)
    }

    override fun onProductVariationOrderRemoved(productDetailVariant: ProductDetailVariant) {
        productDetailViewModel.updateOrderCart(productDetailVariant, false)
    }
}