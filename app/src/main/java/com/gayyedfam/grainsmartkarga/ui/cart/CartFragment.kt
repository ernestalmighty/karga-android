package com.gayyedfam.grainsmartkarga.ui.cart

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gayyedfam.grainsmartkarga.BuildConfig
import com.gayyedfam.grainsmartkarga.R
import com.gayyedfam.grainsmartkarga.ui.components.adapters.OrderListAdapter
import com.gayyedfam.grainsmartkarga.ui.home.OrderBasketState
import com.gayyedfam.grainsmartkarga.ui.orderlist.OrderListViewModel
import com.gayyedfam.grainsmartkarga.ui.profile.ProfileViewState
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.item_delivery_address.*

@AndroidEntryPoint
class CartFragment : Fragment() {

    private val orderListViewModel: OrderListViewModel by viewModels()
    private val orderListAdapter = OrderListAdapter()
    private var interstitialAd: InterstitialAd? = null
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_cart, container, false)

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(), BuildConfig.AD_MOB_FULL_AD_ID, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                interstitialAd = null
            }

            override fun onAdLoaded(p0: InterstitialAd) {
                super.onAdLoaded(p0)
                interstitialAd = p0
            }
        })

        return rootView
    }

    @SuppressLint("ResourceType")
    override fun onResume() {
        super.onResume()
        hideKeyboard()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupList()
        orderListViewModel.getProfile()

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

        imageViewHistory.setOnClickListener {
            orderListViewModel.orderHistory()
        }

        orderListViewModel.orderBasketState.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = View.GONE
            when(it) {
                is OrderBasketState.OrdersLoaded -> {
                    groupEmpty.visibility = View.GONE
                    groupOrders.visibility = View.VISIBLE

                    orderListAdapter.list = it.list
                    orderListAdapter.notifyDataSetChanged()

                    orderListViewModel.orderSummary()
                }
                is OrderBasketState.OrdersSummarized -> {
                    textViewPurchaseTotal.text = it.totalAmount
                    buttonProceed.visibility = View.VISIBLE

                    if(it.deliveryFee == "0.0" || it.deliveryFee == "0") {
                        textViewDeliveryLabel.visibility = View.GONE
                        textViewDeliveryFee.visibility = View.GONE
                        textViewNote.visibility = View.VISIBLE
                    } else {
                        textViewNote.visibility = View.GONE
                        textViewDeliveryLabel.visibility = View.VISIBLE
                        textViewDeliveryFee.visibility = View.VISIBLE

                        textViewDeliveryFee.text = it.deliveryFee
                    }
                }
                is OrderBasketState.OrdersEmpty -> {
                    groupOrders.visibility = View.GONE
                    groupEmpty.visibility = View.VISIBLE

                    val adRequest = AdRequest.Builder().build()
                    adViewContainerCart.loadAd(adRequest)
                }
                is OrderBasketState.OrderSuccessful -> {
                    context?.let { context ->
                        val dialog = MaterialAlertDialogBuilder(context)
                            .setTitle("Order successful")
                            .setMessage("Ref: ${it.referenceId}\n\nYou can paste the reference via FB messenger to chat with our admin.\n\nOtherwise wait on our call for confirmation.")
                            .setNegativeButton("Dismiss", DialogInterface.OnClickListener { dialog, i ->
                                dialog.dismiss()

                                interstitialAd?.show(activity as Activity)
                            })
                            .setPositiveButton("Open Messenger") { _, _ ->
                                val clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clipData = ClipData.newPlainText("reference_no", "Order ref: ${it.referenceId}")
                                clipboardManager.setPrimaryClip(clipData)

                                orderListViewModel.orderFollowUp()
                            }
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
                is OrderBasketState.OrderHistoryLoaded -> {
                    val list = it.list.map { order ->
                        "Date: ${order.date}\nRef: ${order.id}\nAmount: Php${order.totalAmount}\n"
                    }.toTypedArray()

                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Order History")
                        .setIcon(R.drawable.ic_orders_history)
                        .setItems(list
                        ) { _, index ->
                            val order = it.list[index]
                            val clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clipData = ClipData.newPlainText("reference_no", "Order ref: ${order.id}")
                            clipboardManager.setPrimaryClip(clipData)

                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle("Successfully copied reference number")
                                .setMessage("Follow up via FB messenger?")
                                .setPositiveButton("Open Messenger") { _, _ ->
                                    orderListViewModel.orderFollowUp()
                                }
                                .setNegativeButton("Cancel") { refDialog, index ->
                                    refDialog.dismiss()
                                }.show()
                        }
                        .show()
                }
                is OrderBasketState.OrderHistoryEmpty -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Order History")
                        .setMessage("No orders yet")
                        .show()
                }
                is OrderBasketState.OrderFollowUp -> {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.data = Uri.parse(it.messengerLink)
                    startActivity(intent)
                }
                is OrderBasketState.NoMessengerLink -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setMessage("The store has not setup a Facebook page yet. Stay tuned!")
                        .show()
                }
                else -> {}
            }
        })

        orderListViewModel.profileState.observe(viewLifecycleOwner, Observer {
            when(it) {
                is ProfileViewState.ProfileLoaded -> {
                    valueDeliveryAddress.text = "${it.profile.address2} ${it.profile.address1}"
                }
                else -> {}
            }
        })
    }

    private fun setupList() {
        recyclerViewOrders.adapter = orderListAdapter
        recyclerViewOrders.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private fun hideKeyboard() {
        val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusedView = activity?.currentFocus
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}