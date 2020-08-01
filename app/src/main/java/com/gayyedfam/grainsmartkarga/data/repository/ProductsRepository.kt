package com.gayyedfam.grainsmartkarga.data.repository

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import com.gayyedfam.grainsmartkarga.data.local.ProductDAO
import com.gayyedfam.grainsmartkarga.data.model.*
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


/**
 * Created by emgayyed on 18/7/20.
 */
class ProductsRepository @Inject constructor(private val productDAO: ProductDAO) {

    fun getProducts(): Single<List<ProductWithDetail>> {
        val returnList = mutableListOf<ProductWithDetail>()

        return Single.create<List<ProductWithDetail>> {
            val db = Firebase.firestore
            db.collection("inventory")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val id = document.id
                        val productName = document.data["displayName"].toString()
                        val iconUrl = document.data["iconUrl"].toString()
                        val type = document.data["type"] as Long
                        val status = document.data["status"] as Boolean
                        val productType = ProductType.fromInt(Integer.valueOf(type.toInt()))

                        val product = Product(
                            productId = id,
                            name = productName,
                            productType = productType,
                            iconUrl = iconUrl,
                            status = status
                        )

                        returnList.add(ProductWithDetail(product))

                        Observable.create<Boolean> {
                            productDAO.insert(product)
                        }
                            .subscribeOn(Schedulers.io())
                            .subscribe()
                    }

                    it.onSuccess(returnList)
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents.", exception)
                }
        }
    }

    fun getProductDetails(productId: String): Single<List<ProductDetailWithVariants>> {
        val productDetailList = mutableListOf<ProductDetailWithVariants>()

        return Single.create<List<ProductDetailWithVariants>> {
            val db = Firebase.firestore
            db.collection("inventory").document(productId).collection("types")
                .get()
                .continueWithTask { result ->
                    val taskList: MutableList<Task<QuerySnapshot>> = mutableListOf()

                    result.let {
                        it.result?.forEach { snap ->
                            val id = snap.id
                            val displayName = snap.data["displayName"].toString()
                            val description = snap.data["description"].toString()
                            val imageUrl = snap.data["iconUrl"].toString()

                            val productDetail = ProductDetail(
                                productDetailId = id,
                                productIdParent = productId,
                                productDetailName = displayName,
                                productDetailDescription = description,
                                productDetailImage = imageUrl
                            )

                            productDetailList.add(ProductDetailWithVariants(productDetail))

                            Observable.create<Boolean> {
                                productDAO.insert(productDetail)
                            }
                                .subscribeOn(Schedulers.io())
                                .subscribe()

                            taskList.add(
                                db.collection("inventory")
                                    .document(productId)
                                    .collection("types")
                                    .document(id).collection("variants").get()
                            )
                        }

                        Tasks.whenAllComplete(taskList)
                    }
                }
                .addOnSuccessListener { result ->
                    result.forEachIndexed { index, task ->
                        val querySnapshot = task.result as QuerySnapshot

                        val variantsList = mutableListOf<ProductDetailVariant>()
                        for(document in querySnapshot) {
                            val variantId = document.id
                            val variantName = document.data["displayName"].toString()
                            val variantPrice = document.data["price"] as Long

                            val productDetailVariant = ProductDetailVariant(
                                productDetailVariantId = variantId,
                                productDetailId = productDetailList[index].productDetail.productDetailId,
                                productDetailVariantName = variantName,
                                price = variantPrice.toFloat()
                            )

                            variantsList.add(productDetailVariant)
                        }

                        productDetailList[index].variants = variantsList
                    }

                    it.onSuccess(productDetailList)
                }
                .addOnFailureListener {
                    Log.d("TAG", "On failure")
                }
        }
    }

    fun postOrders(profile: Profile, list: List<OrderGroup>): Single<Boolean> {
        val currentTime = Calendar.getInstance().time

        val dateWithTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm a", Locale.getDefault())
        val formattedTime = dateWithTimeFormat.format(currentTime)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(currentTime)

        return Single.create<Boolean> { emitter ->
            val db = Firebase.firestore

            list.forEach {
                val productOrder = it.list[0]

                val order = hashMapOf(
                    "client" to profile.name,
                    "contact" to profile.contact,
                    "address" to profile.address,
                    "type" to productOrder.type,
                    "category" to productOrder.category,
                    "variant" to productOrder.variant,
                    "price" to productOrder.price,
                    "quantity" to it.list.size,
                    "timestamp" to formattedTime
                )

                db.collection("orders").document(formattedDate).collection(profile.deviceId)
                    .add(order)
                    .addOnSuccessListener {
                        emitter.onSuccess(true)
                    }
                    .addOnFailureListener { ex ->
                        emitter.onError(ex)
                    }
            }
        }
    }

    fun addToOrderBasket(productOrder: ProductOrder): Single<Boolean> {
        return Single.create<Boolean> {
            productDAO.insert(productOrder)
        }
    }

    fun updateOrderBasket(productOrder: ProductOrder): Single<Boolean> {
        return Single.create<Boolean> {
            productDAO.updateProductOrder(productOrder.productDetailVariantId)
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

    fun getOrders(detailVariantId: String): Single<List<ProductOrder>> {
        return productDAO
            .getOrdersByProduct(detailVariantId)
    }

    fun deleteAllOrders(): Single<Boolean> {
        return Single.create<Boolean> {
            productDAO.deleteAllOrders()
            it.onSuccess(true)
        }
    }

    fun getStoreList(): Single<List<Store>> {
        return Single.create<List<Store>> { emitter ->
            val db = Firebase.firestore
            db.collection("stores")
                .get()
                .addOnSuccessListener { result ->
                    val list = mutableListOf<Store>()
                    for (document in result) {
                        val id = document.id
                        val name = document.data["displayName"].toString()
                        val address = document.data["address"].toString()

                        val store = Store(
                            storeId = id,
                            name = name,
                            address = address
                        )

                        list.add(store)
                    }

                    emitter.onSuccess(list)
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting store list.", exception)
                }
        }
    }
}