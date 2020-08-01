package com.gayyedfam.grainsmartkarga.ui.cart

import android.annotation.SuppressLint
import android.content.DialogInterface
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
import com.gayyedfam.grainsmartkarga.ui.components.adapters.OrderListAdapter
import com.gayyedfam.grainsmartkarga.ui.home.OrderBasketState
import com.gayyedfam.grainsmartkarga.ui.orderlist.OrderListViewModel
import com.gayyedfam.grainsmartkarga.ui.profile.ProfileViewState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.item_delivery_address.*

@AndroidEntryPoint
class CartFragment : Fragment() {

    private val orderListViewModel: OrderListViewModel by viewModels()
    private val orderListAdapter = OrderListAdapter()
    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_cart, container, false)
        return rootView
    }

    @SuppressLint("ResourceType")
    override fun onResume() {
        super.onResume()

        setupList()
        imageViewBack.setOnClickListener {
            findNavController().popBackStack()
        }

        layoutDeliveryAddress.setOnClickListener {
            val direction = CartFragmentDirections.actionCartFragmentToProfileFragment()
            findNavController().navigate(direction)
        }

        buttonProceed.setOnClickListener {
            context?.let { context ->
                val dialog = MaterialAlertDialogBuilder(context)
                    .setMessage("Please review your order and delivery address before proceeding.")
                    .setPositiveButton("Confirm", DialogInterface.OnClickListener { dialog, i ->
                        dialog.dismiss()
                        orderListViewModel.orderConfirmation()
                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, i ->
                        dialog.dismiss()
                    })
                dialog.show()
            }
        }

        orderListViewModel.getProfile()
        orderListViewModel.load()
        orderListViewModel.orderBasketState.observeForever {
            progressBar.visibility = View.GONE
            when(it) {
                is OrderBasketState.OrdersLoaded -> {
                    groupEmpty.visibility = View.GONE
                    groupOrders.visibility = View.VISIBLE
                    orderListViewModel.orderSummary(it.list)
                }
                is OrderBasketState.OrdersSummarized -> {
                    textViewPurchaseTotal.text = it.totalAmount
                    orderListAdapter.list = it.list
                    orderListAdapter.notifyDataSetChanged()
                }
                is OrderBasketState.OrdersEmpty -> {
                    groupOrders.visibility = View.GONE
                    groupEmpty.visibility = View.VISIBLE
                }
                is OrderBasketState.OrderSuccessful -> {
                    context?.let { context ->
                        val dialog = MaterialAlertDialogBuilder(context)
                            .setMessage("Your order has been logged successfully. Please wait on our call for confirmation")
                            .setPositiveButton("Done", DialogInterface.OnClickListener { dialog, i ->
                                dialog.dismiss()
                                findNavController().popBackStack()
                            })
                        dialog.show()
                    }
                }
                is OrderBasketState.OrderError -> {
                    Snackbar.make(rootView, it.message, Snackbar.LENGTH_SHORT).show()
                }
                is OrderBasketState.OrderLoading -> {
                    if(it.loading) {
                        progressBar.visibility = View.VISIBLE
                    } else {
                        progressBar.visibility = View.GONE
                    }
                }
            }
        }

        orderListViewModel.profileState.observeForever {
            when(it) {
                is ProfileViewState.ProfileLoaded -> {
                    valueDeliveryAddress.text = it.profile.address
                }
            }
        }
    }

    private fun setupList() {
        recyclerViewOrders.adapter = orderListAdapter
        recyclerViewOrders.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }
}