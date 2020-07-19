package com.gayyedfam.grainsmartkarga.data.local

import androidx.room.*
import com.gayyedfam.grainsmartkarga.data.model.Product
import com.gayyedfam.grainsmartkarga.data.model.ProductDetail
import com.gayyedfam.grainsmartkarga.data.model.ProductDetailVariant
import com.gayyedfam.grainsmartkarga.data.model.ProductOrder
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
    fun deleteFromOrder(detailVariantId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<ProductOrder>)

    @Query("SELECT * from product")
    fun getAllProducts(): Single<List<Product>>

    @Query("SELECT * from productdetail WHERE productId = :id")
    fun getProductsDetails(id: Int): Single<List<ProductDetail>>

    @Query("SELECT * from productdetailvariant WHERE productDetailId = :id")
    fun getProductsDetailVariants(id: Int): Single<List<ProductDetailVariant>>

    @Query("SELECT * from productorder")
    fun getAllOrders(): Single<List<ProductOrder>>

    @Query("SELECT * from productorder WHERE productDetailVariantId = :detailVariantId")
    fun getOrdersByProduct(detailVariantId: Int): Single<List<ProductOrder>>

    @Query("""
        SELECT * FROM product WHERE productId = 
        (SELECT productId FROM productdetail WHERE productDetailId = 
        (SELECT productDetailId FROM productdetailvariant WHERE productDetailVariantId = 
        (SELECT * FROM productorder WHERE productOrderId = :orderId)))""")
    fun getProductFromOrderId(orderId: Int): Single<Product>
}