package com.gayyedfam.grainsmartkarga.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gayyedfam.grainsmartkarga.R
import com.gayyedfam.grainsmartkarga.ui.home.OrderBasketState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_cart.*
@AndroidEntryPoint
class CartFragment : Fragment() {

    private val cartViewModel: CartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onResume() {
        super.onResume()

        imageViewBack.setOnClickListener {
            findNavController().popBackStack()
        }

        cartViewModel.load()
        cartViewModel.orderBasketState.observeForever {
            when(it) {
                is OrderBasketState.OrdersLoaded -> {

                }
            }
        }
    }

}