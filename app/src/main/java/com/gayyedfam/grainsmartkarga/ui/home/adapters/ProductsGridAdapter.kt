package com.gayyedfam.grainsmartkarga.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gayyedfam.grainsmartkarga.R
import com.gayyedfam.grainsmartkarga.data.model.Product
import com.gayyedfam.grainsmartkarga.data.model.ProductType
import com.gayyedfam.grainsmartkarga.ui.home.listeners.ProductsItemListener
import kotlinx.android.synthetic.main.item_product_card_grid.view.*

/**
 * Created by emgayyed on 17/7/20.
 */
class ProductsGridAdapter(val productsItemListener: ProductsItemListener): RecyclerView.Adapter<ProductsGridAdapter.ProductCardViewHolder>() {

    var productsList = listOf<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCardViewHolder {
        // grid view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_card_grid, parent, false)

        // list view
        //val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_card_list, parent, false)
        return ProductCardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    override fun onBindViewHolder(holder: ProductCardViewHolder, position: Int) {
        val product = productsList[position]
        holder.bind(product)
    }

    inner class ProductCardViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(product: Product) {
            itemView.textViewName.text = product.name
            when(product.productType) {
                ProductType.WATER -> {
                    Glide
                        .with(itemView)
                        .load(R.drawable.img_tubig)
                        .into(itemView.imageViewProduct)
                }
                ProductType.RICE -> {
                    Glide
                        .with(itemView)
                        .load(R.drawable.img_bigas)
                        .into(itemView.imageViewProduct)
                }
                ProductType.BILLS_PAYMENT -> {
                    Glide
                        .with(itemView)
                        .load(R.drawable.img_bills_payment)
                        .into(itemView.imageViewProduct)
                }
                else -> {
                    Glide
                        .with(itemView)
                        .load(R.drawable.ic_launcher_background)
                        .into(itemView.imageViewProduct)
                }
            }

            itemView.setOnClickListener {
                productsItemListener.onProductClicked(product)
            }
        }
    }
}