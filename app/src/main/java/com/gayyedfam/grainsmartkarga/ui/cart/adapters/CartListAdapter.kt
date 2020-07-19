package com.gayyedfam.grainsmartkarga.ui.cart.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gayyedfam.grainsmartkarga.R
import com.gayyedfam.grainsmartkarga.data.model.ProductOrder

/**
 * Created by emgayyed on 19/7/20.
 */
class CartListAdapter: RecyclerView.Adapter<CartListAdapter.CartViewHolder>() {

    val list = listOf<ProductOrder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_summary, parent, false)

        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val productOrder = list[position]
        holder.bind(productOrder)
    }

    inner class CartViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(productOrder: ProductOrder) {

        }
    }
}