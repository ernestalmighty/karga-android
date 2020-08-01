package com.gayyedfam.grainsmartkarga.ui.productdetail.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gayyedfam.grainsmartkarga.R
import com.gayyedfam.grainsmartkarga.data.model.ProductDetail
import com.gayyedfam.grainsmartkarga.data.model.ProductDetailVariant
import com.gayyedfam.grainsmartkarga.data.model.ProductDetailWithVariants
import com.gayyedfam.grainsmartkarga.data.model.ProductOrder
import com.gayyedfam.grainsmartkarga.ui.home.listeners.ProductsItemPricingListener
import kotlinx.android.synthetic.main.item_order_summary.view.*
import kotlinx.android.synthetic.main.item_product_card_list.view.*

/**
 * Created by emgayyed on 18/7/20.
 */
class ProductsDetailListAdapter(val productsItemPricingListener: ProductsItemPricingListener): RecyclerView.Adapter<ProductsDetailListAdapter.ProductDetailViewHolder>() {

    var list = listOf<ProductDetailWithVariants>()
    var ordersList = listOf<ProductOrder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_card_list, parent, false)
        return ProductDetailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductDetailViewHolder, position: Int) {
        val productDetail = list[position]
        holder.bind(productDetail)
    }

    inner class ProductDetailViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(productDetail: ProductDetailWithVariants) {
            val productsPricingListAdapter = ProductPricingListAdapter(productsItemPricingListener, ordersList, productDetail.productDetail.productDetailName)
            itemView.recyclerViewPricing.adapter = productsPricingListAdapter
            itemView.recyclerViewPricing.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)

            itemView.textViewName.text = productDetail.productDetail.productDetailName

            val description = productDetail.productDetail.productDetailDescription
            if(description == "null") {
                itemView.textViewDescription.visibility = View.GONE
            } else {
                itemView.textViewDescription.visibility = View.VISIBLE
                itemView.textViewDescription.text = description
            }

            Glide.with(itemView)
                .load(productDetail.productDetail.productDetailImage)
                .into(itemView.imageViewProduct)

            productsPricingListAdapter.list = productDetail.variants
            productsPricingListAdapter.notifyDataSetChanged()
        }
    }
}