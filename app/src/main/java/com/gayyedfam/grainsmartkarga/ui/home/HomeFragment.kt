package com.gayyedfam.grainsmartkarga.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.gayyedfam.grainsmartkarga.R
import com.gayyedfam.grainsmartkarga.data.model.Product
import com.gayyedfam.grainsmartkarga.ui.home.adapters.ProductsGridAdapter
import com.gayyedfam.grainsmartkarga.ui.home.listeners.ProductsItemListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_product_pricing.*

@AndroidEntryPoint
class HomeFragment : Fragment(), ProductsItemListener {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        super.onResume()

        homeViewModel.homeStateLiveData.observeForever {
            when(it) {
                is HomeState.ProductsLoaded -> {
                    setupListView(it.list)
                }
                is HomeState.ProductLoadingError -> {

                }
            }
        }

        homeViewModel.basketStateLiveData.observeForever {
            when(it) {
                is OrderBasketState.OrdersLoaded -> {
                    setBasketQuantity(it.list.size.toString())
                }
                is OrderBasketState.OrdersLoadError -> {
                    setBasketQuantity("0")
                }
            }
        }

        homeViewModel.load()

        imageViewBasket.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeFragmentToCartFragment()
            findNavController().navigate(direction)
        }
    }

    private fun setBasketQuantity(value: String) {
        textViewOrderBadge.text = value
    }

    private fun setupListView(list: List<Product>) {
        val productsGridAdapter = ProductsGridAdapter(this)

        recyclerViewProducts.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        recyclerViewProducts.adapter = productsGridAdapter

        productsGridAdapter.productsList = list
        productsGridAdapter.notifyDataSetChanged()
    }

    override fun onProductClicked(product: Product) {
        val directions = HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(
            product.productId,
            product.name
        )
        findNavController().navigate(directions)
    }
}