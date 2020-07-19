package com.gayyedfam.grainsmartkarga.ui.productdetail.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gayyedfam.grainsmartkarga.R
import com.gayyedfam.grainsmartkarga.data.model.ProductDetailVariant
import com.gayyedfam.grainsmartkarga.data.model.ProductOrder
import com.gayyedfam.grainsmartkarga.ui.home.listeners.ProductsItemPricingListener
import kotlinx.android.synthetic.main.item_product_pricing.view.*

/**
 * Created by emgayyed on 18/7/20.
 */
class ProductPricingListAdapter(val listener: ProductsItemPricingListener, val orders: List<ProductOrder> = listOf()): RecyclerView.Adapter<ProductPricingListAdapter.ProductPricingViewHolder>() {

    var list = listOf<ProductDetailVariant>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductPricingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_pricing, parent, false)
        return ProductPricingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductPricingViewHolder, position: Int) {
        val productPricingOrder = list[position]
        holder.bind(productPricingOrder)
    }

    inner class ProductPricingViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(productDetailVariant: ProductDetailVariant) {
            val name = productDetailVariant.productDetailVariantName
            val price = productDetailVariant.price
            itemView.textViewProductVariation.text = "$name (Php ${price})"

            val orderList = orders.filter {
                it.productDetailVariantId == productDetailVariant.productDetailVariantId
            }

            var quantity = orderList.size

            itemView.textViewQuantity.text = quantity.toString()

            itemView.imageViewAdd.setOnClickListener {
                quantity++
                itemView.textViewQuantity.text = quantity.toString()
                listener.onProductVariationOrderAdded(productDetailVariant)
            }

            itemView.imageViewRemove.setOnClickListener {
                if(quantity > 0) {
                    quantity--
                    itemView.textViewQuantity.text = quantity.toString()
                    listener.onProductVariationOrderRemoved(productDetailVariant)
                }
            }
        }
    }
}