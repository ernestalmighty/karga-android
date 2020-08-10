package com.gayyedfam.grainsmartkarga.ui.home

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.gayyedfam.grainsmartkarga.BuildConfig
import com.gayyedfam.grainsmartkarga.R
import com.gayyedfam.grainsmartkarga.data.model.Product
import com.gayyedfam.grainsmartkarga.data.model.ProductWithDetail
import com.gayyedfam.grainsmartkarga.data.model.Store
import com.gayyedfam.grainsmartkarga.ui.home.adapters.ProductsGridAdapter
import com.gayyedfam.grainsmartkarga.ui.home.listeners.ProductsItemListener
import com.gayyedfam.grainsmartkarga.utils.isNetworkConnected
import com.google.android.gms.ads.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_karga_branch.*

@AndroidEntryPoint
class HomeFragment : Fragment(), ProductsItemListener {

    private val homeViewModel: HomeViewModel by viewModels()
    private var isShowingDialog = false
    private val adRequest = AdRequest.Builder().build()

    private val storeStateObserver = Observer<StoreState> {
        when(it) {
            is StoreState.Nothing -> {

            }
            is StoreState.StoresLoaded -> {
                lottieEmptyBag.visibility = View.GONE
                val list = it.list.map { store ->
                    store.name
                }.toTypedArray()

                showChangeStoreDialog(list, it.list)
            }
            is StoreState.UserStoreLoaded -> {
                valueBranch.text = it.store.name
                valueBranchAddress.text = it.store.address

                it.store.contact?.let { text ->
                    valueBranchContact.text = text
                    valueBranchContact.visibility = View.VISIBLE
                } ?: {
                    valueBranchContact.visibility = View.GONE
                }()

                it.store.social?.let { text ->
                    valueBranchSocial.text = text
                    valueBranchSocial.visibility = View.VISIBLE
                } ?: {
                    valueBranchSocial.visibility = View.GONE
                }()
            }
            is StoreState.UserStoreEmpty -> {
                context?.let { ctx ->
                    if(ctx.isNetworkConnected()) {
                        homeViewModel.emptyUserStore()
                    } else {
                        showNoInternetConnectionDialog()
                    }
                }
            }
            is StoreState.UserStoreSelected -> {
                homeViewModel.load(it.store)
            }
            is StoreState.StoreListEmpty -> {
                lottieEmptyBag.visibility = View.VISIBLE
                showStoreEmptyDialog()
            }
        }
    }

    private fun showNoInternetConnectionDialog(): AlertDialog? {
        return MaterialAlertDialogBuilder(context)
            .setTitle("Your internet is not connected")
            .setMessage("Please ensure you have a stable network connection to get the latest products.")
            .setPositiveButton("Retry") { dialogInterface, i ->
                dialogInterface.dismiss()
                homeViewModel.loadUserStore()
            }
            .setNegativeButton("Cancel") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun showStoreEmptyDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle("Sorry!")
            .setMessage("We cannot load the stores as of the moment. Please try again later.")
            .setPositiveButton("Retry") { dialogInterface, i ->
                dialogInterface.dismiss()
                homeViewModel.emptyUserStore()
            }
            .setNegativeButton("Cancel") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun showChangeStoreDialog(
        list: Array<String>,
        stores: List<Store>
    ) {
        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle("Choose your branch:")
            .setItems(list) { _, index ->

                MaterialAlertDialogBuilder(context)
                    .setMessage("Selecting another store will clear your cart. Do you want to switch branch?")
                    .setPositiveButton(
                        "YES",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            val store = stores[index]

                            valueBranch.text = store.name
                            valueBranchAddress.text = store.address

                            store.contact?.let { text ->
                                valueBranchContact.text = text
                                valueBranchContact.visibility = View.VISIBLE
                            } ?: {
                                valueBranchContact.visibility = View.GONE
                            }()

                            store.social?.let { text ->
                                valueBranchSocial.text = text
                                valueBranchSocial.visibility = View.VISIBLE
                            } ?: {
                                valueBranchSocial.visibility = View.GONE
                            }()

                            homeViewModel.clearOrders()
                            homeViewModel.storeSelected(store)

                            isShowingDialog = false
                            dialogInterface.dismiss()
                        })
                    .setNegativeButton(
                        "CANCEL"
                    ) { dialogInterface, i ->
                        dialogInterface.dismiss()
                    }.show()
            }

        if (!isShowingDialog) {
            isShowingDialog = true
            dialog.show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        super.onResume()

        homeViewModel.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adView = AdView(context)
        adView.adSize = AdSize.SMART_BANNER
        adView.adUnitId = BuildConfig.AD_MOB_BANNER_ID
        adView.adListener = object : AdListener() {
            override fun onAdImpression() {
                super.onAdImpression()
            }

            override fun onAdLeftApplication() {
                super.onAdLeftApplication()
            }

            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
            }

            override fun onAdFailedToLoad(p0: LoadAdError?) {
                super.onAdFailedToLoad(p0)
                groupCart.visibility = View.VISIBLE
            }

            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                groupCart.visibility = View.VISIBLE
            }
        }

        adViewContainer.addView(adView)

        adView.loadAd(adRequest)

        homeViewModel.homeStateLiveData.observe(viewLifecycleOwner, Observer {
            when(it) {
                is HomeState.ProductsLoaded -> {
                    setupListView(it.list)
                }
                is HomeState.ProductLoadingError -> {

                }
                is HomeState.ProductLoadingProgress -> {
                    if(it.isLoading) {
                        progressBar.visibility = View.VISIBLE
                    } else {
                        progressBar.visibility = View.GONE
                    }
                }
            }
        })

        homeViewModel.storeStateLiveData.observe(viewLifecycleOwner, storeStateObserver)

        homeViewModel.basketStateLiveData.observe(viewLifecycleOwner, Observer {
            when(it) {
                is OrderBasketState.OrdersLoaded -> {
                    setBasketQuantity(it.list.size.toString())
                }
                is OrderBasketState.OrdersLoadError -> {
                    setBasketQuantity("0")
                }
            }
        })

        fabBasket.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeFragmentToCartFragment()
            findNavController().navigate(direction)
        }

        layoutDeliveryAddress.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
            findNavController().navigate(direction)
        }

        layoutKargaBranch.setOnClickListener {
            isShowingDialog = false
            homeViewModel.loadStore()
        }
    }

    private fun setBasketQuantity(value: String) {
        textViewOrderBadge.text = value
    }

    private fun setupListView(list: List<ProductWithDetail>) {
        val productsGridAdapter = ProductsGridAdapter(this)

        recyclerViewProducts.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        recyclerViewProducts.adapter = productsGridAdapter

        productsGridAdapter.productsList = list
        productsGridAdapter.notifyDataSetChanged()
    }

    override fun onProductClicked(product: Product) {
        val extras = FragmentNavigatorExtras(
            imageViewBasket to "cartProductDetailTransition"
        )
        val directions = HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(
            product.productId,
            product.name,
            product.productType,
            product.iconUrl
        )
        findNavController().navigate(directions, extras)
    }
}