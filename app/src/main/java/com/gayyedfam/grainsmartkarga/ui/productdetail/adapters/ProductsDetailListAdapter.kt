package com.gayyedfam.grainsmartkarga.ui.productdetail.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gayyedfam.grainsmartkarga.R
import com.gayyedfam.grainsmartkarga.data.model.ProductDetailWithVariants
import com.gayyedfam.grainsmartkarga.data.model.ProductOrder
import com.gayyedfam.grainsmartkarga.ui.home.listeners.ProductsItemPricingListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_product_detail.*
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
            itemView.recyclerViewPricing.addItemDecoration(
                DividerItemDecoration(
                    itemView.context,
                    LinearLayoutManager.HORIZONTAL
                )
            )
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

            itemView.setOnClickListener {
                val layoutInflater = LayoutInflater.from(itemView.context)
                val view = layoutInflater.inflate(R.layout.item_product_detail_image, null)

                val imageIcon = view.findViewById<AppCompatImageView>(R.id.imageViewProduct)
                Glide.with(view)
                    .load(productDetail.productDetail.productDetailImage)
                    .into(imageIcon)

                MaterialAlertDialogBuilder(itemView.context)
                    .setView(view)
                    .show()
            }

            productsPricingListAdapter.list = productDetail.variants
            productsPricingListAdapter.notifyDataSetChanged()
        }
    }
}