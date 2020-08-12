package com.gayyedfam.grainsmartkarga.data.repository

import android.location.Location
import android.util.Log
import com.gayyedfam.grainsmartkarga.data.local.ProductDAO
import com.gayyedfam.grainsmartkarga.data.local.ProfileDAO
import com.gayyedfam.grainsmartkarga.data.model.*
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


/**
 * Created by emgayyed on 18/7/20.
 */
class ProductsRepository @Inject constructor(private val productDAO: ProductDAO,
                                             private val profileDAO: ProfileDAO) {

    fun getProducts(storeId: String): Single<List<ProductWithDetail>> {
        val returnList = mutableListOf<ProductWithDetail>()

        return Single.create<List<ProductWithDetail>> {
            val db = Firebase.firestore
            db.collection("stores").document(storeId).collection("inventory")
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
            val store = productDAO.getStore().blockingGet()
            val db = Firebase.firestore
            db.collection("stores").document(store.storeId).collection("inventory").document(productId).collection("types")
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
                                db.collection("stores")
                                    .document(store.storeId)
                                    .collection("inventory")
                                    .document(productId)
                                    .collection("types")
                                    .document(id)
                                    .collection("variants").get()
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
                            val stocksLeft = document.data["stock"]

                            var stocks = -1L
                            stocksLeft?.let {
                                stocks = it as Long
                            }

                            val productDetailVariant = ProductDetailVariant(
                                productDetailVariantId = variantId,
                                productDetailId = productDetailList[index].productDetail.productDetailId,
                                productDetailVariantName = variantName,
                                price = variantPrice.toFloat(),
                                stocksLeft = stocks.toInt()
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

    fun postOrders(profile: Profile, list: List<OrderGroup>, totalAmount: String, deliveryFee: String): Single<String> {
        val currentTime = Calendar.getInstance().time

        val dateWithTimeFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
        val formattedTime = dateWithTimeFormat.format(currentTime)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(currentTime)

        var orderDetails: StringBuilder = java.lang.StringBuilder()

        return Single.create<String> { emitter ->
            val db = Firebase.firestore
            val store = productDAO.getStore().blockingGet()

            val ref = db.collection("stores").document(store.storeId).collection("orders")
                .document(formattedDate)
                .collection(profile.deviceId)
                .document()

            list.forEach {
                val productAmount = it.list.map { order ->
                    order.price
                }.sum()

                val productOrder = it.list[0]

                val order = hashMapOf(
                    "Product Type" to productOrder.type,
                    "Category" to productOrder.category,
                    "Variant" to productOrder.variant,
                    "Price" to productOrder.price,
                    "Quantity" to it.list.size,
                    "Total Amount" to productAmount
                )

                db.collection("stores").document(store.storeId).collection("orders")
                    .document(formattedDate)
                    .collection(profile.deviceId)
                    .document(ref.id)
                    .collection("list")
                    .add(order)
                    .addOnSuccessListener { docRef ->
                        emitter.onSuccess(ref.id)

                        val htmlBuilder = StringBuilder()
                        htmlBuilder.append("<p><b>Order Detail:</b></p>")
                        htmlBuilder.append("<table>")

                        for ((key, value) in order.entries) {
                            htmlBuilder.append(
                                String.format(
                                    "<tr><td>%s</td><td>%s</td></tr>",
                                    key, value
                                )
                            )
                        }

                        htmlBuilder.append("</table>")

                        orderDetails.append(htmlBuilder.toString())
                    }
                    .addOnFailureListener { ex ->
                        emitter.onError(ex)
                    }
            }

            val totalMap = hashMapOf(
                "Total Amount" to totalAmount,
                "Delivery Fee" to deliveryFee,
                "Date" to currentTime.toString(),
                "Client Name" to profile.name,
                "Contact" to profile.contact,
                "Address" to profile.address,
                "Delivery Instructions" to profile.deliveryInstruction,
                "Status" to "unconfirmed"
            )

            db.collection("stores").document(store.storeId).collection("orders")
                .document(formattedDate)
                .collection(profile.deviceId)
                .document(ref.id)
                .set(totalMap)
                .addOnSuccessListener {
                    val orderHistory = OrderHistory(
                        ref.id,
                        totalAmount.toFloat(),
                        formattedTime
                    )

                    saveToOrderHistory(orderHistory)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()

                    val htmlBuilder = StringBuilder()
                    htmlBuilder.append("<p><b>Summary</b></p>")
                    htmlBuilder.append("<table>")

                    for ((key, value) in totalMap.entries) {
                        htmlBuilder.append(
                            String.format(
                                "<tr><td>%s</td><td>%s</td></tr>",
                                key, value
                            )
                        )
                    }

                    htmlBuilder.append("</table>")
                    htmlBuilder.append(orderDetails)

                    val html = htmlBuilder.toString()

                    val messageMap = hashMapOf(
                        "subject" to "You have a new order!",
                        "text" to "Grainsmart ${store.name}",
                        "html" to html
                    )

                    val mailMap = hashMapOf(
                        "to" to "kargadeliveryph@gmail.com",
                        "message" to messageMap,
                        "cc" to "${store.email}"
                    )

                    db.collection("mail")
                        .document()
                        .set(mailMap)
                        .addOnSuccessListener {

                        }
                        .addOnFailureListener {

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
                        val email = document.data["email"].toString()
                        var contact = document.data["contact"]
                        var social = document.data["social"]
                        var messenger = document.data["messenger"]
                        var lat = document.data["lat"]
                        var lon = document.data["lon"]

                        if(lat == null) {
                            lat = 0
                        }

                        if(lon == null) {
                            lon = 0
                        }

                        var contactValue = ""
                        var socialValue = ""
                        var messengerValue = ""
                        contact?.let {
                            contactValue = it.toString()
                        }

                        social?.let {
                            socialValue = it.toString()
                        }

                        messenger?.let {
                            messengerValue = it.toString()
                        }

                        val store = Store(
                            storeId = id,
                            name = name,
                            address = address,
                            lat = lat as Double,
                            lon = lon as Double,
                            contact = contactValue,
                            social = socialValue,
                            email = email,
                            messenger = messengerValue
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

    fun getDeliveryFee(): Single<DeliveryFee> {
        return Single.create<DeliveryFee> {
            val db = Firebase.firestore
            val store = productDAO.getStore().blockingGet()
            db.collection("stores").document(store.storeId).collection("fees")
                .get()
                .addOnSuccessListener { result ->
                    val deliveryFee = DeliveryFee()
                    for (document in result) {
                        val id = document.id
                        val isEnabled = document.data["deliveryFeeEnabled"] as Boolean
                        val radiusFree = document.data["deliveryFeeRadiusFree"] as Long
                        val deliveryExtra = document.data["deliveryExtraPeso"] as Long
                        val displayName = document.data["displayName"] as String

                        deliveryFee.displayName = displayName
                        deliveryFee.isEnabled = isEnabled
                        deliveryFee.freeRadius = radiusFree.toInt()
                        deliveryFee.deliveryExtra = deliveryExtra.toInt()
                    }

                    it.onSuccess(deliveryFee)
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents.", exception)
                }
        }
    }

    fun getStore(): Single<Store> {
        return productDAO.getStore()
    }

    fun getDistanceDeviceStore(): Single<Double> {
        return Single.create<Double> {

            val deviceLocation = profileDAO.getDeviceLocation().blockingGet()
            val storeLocation = productDAO.getStore().blockingGet()

            val results = FloatArray(1)
            Location.distanceBetween(storeLocation.lat,
                storeLocation.lon,
            deviceLocation.locationLat, deviceLocation.locationLon, results)

            it.onSuccess(results[0].toDouble())
        }
    }

    fun saveStore(store: Store): Single<Boolean> {
        return Single.create<Boolean> {
            productDAO.deleteStore()
            productDAO.insert(store)
            it.onSuccess(true)
        }
    }

    fun getOrderHistory(): Single<List<OrderHistory>> {
        return productDAO.getOrderHistory()
    }

    fun saveToOrderHistory(orderHistory: OrderHistory): Single<Boolean> {
        return Single.create<Boolean> {
            productDAO.insert(orderHistory)
            it.onSuccess(true)
        }
    }
}