package com.gayyedfam.grainsmartkarga.ui.productdetail.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gayyedfam.grainsmartkarga.R
import com.gayyedfam.grainsmartkarga.data.model.ProductDetail
import com.gayyedfam.grainsmartkarga.data.model.ProductDetailVariant
import com.gayyedfam.grainsmartkarga.data.model.ProductOrder
import com.gayyedfam.grainsmartkarga.ui.home.listeners.ProductsItemPricingListener
import kotlinx.android.synthetic.main.item_product_card_list.view.*

/**
 * Created by emgayyed on 18/7/20.
 */
class ProductsDetailListAdapter(val productsItemPricingListener: ProductsItemPricingListener): RecyclerView.Adapter<ProductsDetailListAdapter.ProductDetailViewHolder>() {

    var list = listOf<ProductDetail>()
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

    fun updateProductDetail(productDetailId: Int, variants: List<ProductDetailVariant>) {
        val productPosition = list.indexOfFirst {
            it.productDetailId == productDetailId
        }

        val productDetail = list[productPosition]
        productDetail.variants = variants

        notifyItemChanged(productPosition)
    }

    inner class ProductDetailViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(productDetail: ProductDetail) {
            val productsPricingListAdapter = ProductPricingListAdapter(productsItemPricingListener, ordersList)
            itemView.recyclerViewPricing.adapter = productsPricingListAdapter
            itemView.recyclerViewPricing.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)

            itemView.textViewName.text = productDetail.productDetailName
            productsPricingListAdapter.list = productDetail.variants
            productsPricingListAdapter.notifyDataSetChanged()
        }
    }
}