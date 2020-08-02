package com.gayyedfam.grainsmartkarga.ui.home.adapters

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gayyedfam.grainsmartkarga.R
import com.gayyedfam.grainsmartkarga.data.model.Product
import com.gayyedfam.grainsmartkarga.data.model.ProductType
import com.gayyedfam.grainsmartkarga.data.model.ProductWithDetail
import com.gayyedfam.grainsmartkarga.ui.home.listeners.ProductsItemListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.item_product_card_grid.view.*

/**
 * Created by emgayyed on 17/7/20.
 */
class ProductsGridAdapter(val productsItemListener: ProductsItemListener): RecyclerView.Adapter<ProductsGridAdapter.ProductCardViewHolder>() {

    var productsList = listOf<ProductWithDetail>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCardViewHolder {
        // grid view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_card_grid, parent, false)

        return ProductCardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    override fun onBindViewHolder(holder: ProductCardViewHolder, position: Int) {
        val product = productsList[position].product
        holder.bind(product)
    }

    inner class ProductCardViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(product: Product) {
            itemView.textViewName.text = product.name
            Glide
                .with(itemView)
                .load(product.iconUrl)
                .into(itemView.imageViewProduct)

            itemView.setOnClickListener {
                if(product.status) {
                    productsItemListener.onProductClicked(product)
                } else {
                    MaterialAlertDialogBuilder(itemView.context)
                        .setMessage("This feature will be available soon!")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.dismiss()
                        }).show()
                }
            }

            if(product.status) {
                itemView.textViewComingSoon.visibility = View.GONE
            } else {
                itemView.textViewComingSoon.visibility = View.VISIBLE
            }
        }
    }
}