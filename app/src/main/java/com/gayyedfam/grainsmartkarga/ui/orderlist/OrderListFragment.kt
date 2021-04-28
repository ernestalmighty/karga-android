package com.gayyedfam.grainsmartkarga.ui.orderlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gayyedfam.grainsmartkarga.R
import com.gayyedfam.grainsmartkarga.ui.components.adapters.OrderListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderListFragment : Fragment() {

    private val orderListViewModel: OrderListViewModel by viewModels()
    private val orderListAdapter = OrderListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_list, container, false)
    }
}