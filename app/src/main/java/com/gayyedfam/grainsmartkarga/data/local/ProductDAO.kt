package com.gayyedfam.grainsmartkarga.data.local

import androidx.room.*
import com.gayyedfam.grainsmartkarga.data.model.*
import io.reactivex.Single

/**
 * Created by emgayyed on 18/7/20.
 */
@Dao
interface ProductDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(productDetail: ProductDetail)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(productDetailVariant: ProductDetailVariant)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(productOrder: ProductOrder)

    @Query("DELETE FROM productorder WHERE productOrderId = (SELECT productOrderId FROM productorder WHERE productDetailVariantId = :detailVariantId LIMIT 1)")
    fun deleteFromOrder(detailVariantId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<ProductOrder>)

    @Query("SELECT * from product")
    fun getAllProducts(): Single<List<Product>>

    @Query("SELECT * from productdetail WHERE productDetailId = :id")
    fun getProductsDetails(id: Int): Single<List<ProductDetail>>

    @Query("SELECT * from productdetailvariant WHERE productDetailId = :id")
    fun getProductsDetailVariants(id: Int): Single<List<ProductDetailVariant>>

    @Query("SELECT * from productorder")
    fun getAllOrders(): Single<List<ProductOrder>>

    @Query("SELECT * from productorder WHERE productDetailVariantId = :detailVariantId")
    fun getOrdersByProduct(detailVariantId: String): Single<List<ProductOrder>>

    @Transaction
    @Query("SELECT * FROM Product")
    fun getProducts(): Single<List<ProductWithDetail>>

    @Transaction
    @Query("SELECT * FROM ProductDetail WHERE productIdParent = :productId")
    fun getProductDetails(productId: String): Single<List<ProductDetailWithVariants>>

    @Query("UPDATE productorder SET quantity = quantity + 1 WHERE productDetailVariantId = :id")
    fun updateProductOrder(id: String)

    @Query("DELETE FROM productorder")
    fun deleteAllOrders()

    @Query("SELECT * FROM store LIMIT 1")
    fun getStore(): Single<Store>

    @Query("DELETE FROM store")
    fun deleteStore()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(store: Store)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(orderHistory: OrderHistory)

    @Query("SELECT * FROM orderhistory")
    fun getOrderHistory(): Single<List<OrderHistory>>
}