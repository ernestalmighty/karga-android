package com.gayyedfam.grainsmartkarga.data.repository

import com.gayyedfam.grainsmartkarga.data.local.ProductDAO
import com.gayyedfam.grainsmartkarga.data.model.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by emgayyed on 18/7/20.
 */
class ProductsRepository @Inject constructor(private val productDAO: ProductDAO) {
    fun load() {
        val productDetail1 = ProductDetail(1,  1, "Jasmine")
        val productDetail2 = ProductDetail(2,  1, "Dinorado")
        val productDetail3 = ProductDetail(3,  1, "Sinandomeng")
        val productDetail4 = ProductDetail(4,  1, "Long Grain")
        val productDetail5 = ProductDetail(5,  1, "Japanese")

        val variations1 = ProductDetailVariant(1, 1, "Sako", 150.9F)
        val variations2 = ProductDetailVariant(2, 2, "Kilo", 54.0F)
        val variations3 = ProductDetailVariant(3, 1, "Kaban", 14.2F)

        val riceProduct = Product(
            1,
            "Bigas",
            ProductType.RICE)
        val waterProduct = Product(2, "Tubig", ProductType.WATER)
        val billsPaymentProduct = Product(3, "Bills Payment", ProductType.BILLS_PAYMENT)


        Observable.create<Boolean> {
            productDAO.insert(riceProduct)
            productDAO.insert(waterProduct)
            productDAO.insert(billsPaymentProduct)

            productDAO.insert(productDetail1)
            productDAO.insert(productDetail2)
            productDAO.insert(productDetail3)
            productDAO.insert(productDetail4)
            productDAO.insert(productDetail5)

            productDAO.insert(variations1)
            productDAO.insert(variations2)
            productDAO.insert(variations3)
        }
            .subscribeOn(Schedulers.io())
            .subscribe()

    }

    fun getProducts(): Single<List<Product>> {
        return productDAO
            .getAllProducts()
            .map {
                it
            }
    }

    fun getProductDetails(productId: Int): Single<List<ProductDetail>> {
        return productDAO
            .getProductsDetails(productId)
            .map {
                it
            }
    }

    fun getProductDetailVariants(productDetailId: Int): Single<List<ProductDetailVariant>> {
        return productDAO
            .getProductsDetailVariants(productDetailId)
            .map {
                it
            }
    }

    fun addToOrderBasket(productOrder: ProductOrder): Single<Boolean> {
        return Single.create<Boolean> {
            productDAO.insert(productOrder)
        }
    }

    fun removeFromOrderBasket(productOrder: ProductOrder): Single<Boolean> {
        return Single.create<Boolean> {
            productDAO.deleteFromOrder(productOrder.productDetailVariantId)
        }
    }

    fun getOrders(): Single<List<ProductOrder>> {
        return productDAO
            .getAllOrders()
    }

    fun getOrders(detailVariantId: Int): Single<List<ProductOrder>> {
        return productDAO
            .getOrdersByProduct(detailVariantId)
    }
}