package com.gayyedfam.grainsmartkarga.ui.productdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gayyedfam.grainsmartkarga.R
import com.gayyedfam.grainsmartkarga.data.model.ProductDetailVariant
import com.gayyedfam.grainsmartkarga.ui.home.OrderBasketState
import com.gayyedfam.grainsmartkarga.ui.home.listeners.ProductsItemPricingListener
import com.gayyedfam.grainsmartkarga.ui.productdetail.ProductDetailFragmentArgs.Companion.fromBundle
import com.gayyedfam.grainsmartkarga.ui.productdetail.adapters.ProductsDetailListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_product_detail.*

@AndroidEntryPoint
class ProductDetailFragment : Fragment(), ProductsItemPricingListener {

    private val productId by lazy {
        fromBundle(requireArguments()).productId
    }

    private val productName by lazy {
        fromBundle(requireArguments()).productName
    }

    private val productType by lazy {
        fromBundle(requireArguments()).productType
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
        setupList()

        fabBasket.setOnClickListener {
            val direction = ProductDetailFragmentDirections.actionProductDetailFragmentToCartFragment()
            findNavController().navigate(direction)
        }

        productDetailViewModel.productDetailState.observeForever {
            when(it) {
                is ProductDetailState.ProductDetailLoaded -> {
                    productDetailAdapter.list = it.productDetails
                    productDetailAdapter.notifyDataSetChanged()
                }
                is ProductDetailState.ProductDetailLoadError -> {

                }
                is ProductDetailState.ProductDetailLoading -> {
                    if(it.loading) {
                        progressBar.visibility = View.VISIBLE
                    } else {
                        progressBar.visibility = View.GONE
                    }
                }
            }
        }

        productDetailViewModel.orderBasketState.observeForever {
            when(it) {
                is OrderBasketState.OrdersLoaded -> {
                    productDetailAdapter.ordersList = it.list
                    setBasketQuantity(it.list.size.toString())
                }
            }
        }

        productDetailViewModel.load(productId)
        productDetailViewModel.loadOrders()
    }

    private fun setBasketQuantity(value: String) {
        textViewOrderBadge.text = value
    }

    private fun setToolbar() {
        textViewToolbarTitle.text = productName
        imageViewBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupList() {
        recyclerViewProductDetails.adapter = productDetailAdapter
        recyclerViewProductDetails.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun onProductVariationOrderAdded(productDetailVariant: ProductDetailVariant, productDetailName: String) {
        productDetailViewModel.updateOrderCart(true, productDetailVariant, productType, productDetailName)
    }

    override fun onProductVariationOrderRemoved(productDetailVariant: ProductDetailVariant) {
        productDetailViewModel.updateOrderCart(false, productDetailVariant)
    }
}