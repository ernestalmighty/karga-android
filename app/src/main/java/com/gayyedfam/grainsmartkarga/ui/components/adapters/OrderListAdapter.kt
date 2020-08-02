package com.gayyedfam.grainsmartkarga.ui.components.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gayyedfam.grainsmartkarga.R
import com.gayyedfam.grainsmartkarga.data.model.OrderGroup
import com.gayyedfam.grainsmartkarga.data.model.ProductType
import kotlinx.android.synthetic.main.item_order_summary.view.*

/**
 * Created by emgayyed on 19/7/20.
 */
class OrderListAdapter: RecyclerView.Adapter<OrderListAdapter.CartViewHolder>() {

    var list = listOf<OrderGroup>()

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
        fun bind(productOrder: OrderGroup) {
            val order = productOrder.list[0]

            Glide.with(view)
                .load(order.image)
                .into(itemView.imageViewOrderIcon)

            val category = order.category
            val variant = order.variant
            val quantity = productOrder.list.size
            val price = order.price
            val total = order.price * quantity

            itemView.textViewOrderName.text =  "$category"
            itemView.textViewOrderPrice.text = "$total"
            itemView.textViewOrderVariant.text = "$variant @ Php$price"
            itemView.textViewOrderQuantity.text = "Qty: $quantity"
        }
    }
}