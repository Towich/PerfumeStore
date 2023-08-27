package com.example.perfumestore.data.database

import android.util.Log
import com.example.perfumestore.data.model.ProductItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RealtimeDatabase {

    private val database: FirebaseDatabase = Firebase.database
    private var perfumesListReference: DatabaseReference = database.getReference("perfumes")

    fun getDatabase(): FirebaseDatabase = database
    fun getPerfumesListReference(): DatabaseReference = perfumesListReference

    fun updateQuantityOfProduct(productItem: ProductItem, newValue: Int) {
        productItem.quantity = newValue
        perfumesListReference
            .child(productItem.id.toString())
            .child("quantity")
            .setValue(newValue)
    }

    fun updateQuantityOfProducts(productItems: List<ProductItem>) {
        for (product in productItems) {
            perfumesListReference.child(product.id.toString()).child("quantity").get()
                .addOnSuccessListener {
                    Log.i("RealtimeDatabase", "Got value: ${it.value}")
                    val oldQuantity = (it.value as Long).toInt()

                    perfumesListReference
                        .child(product.id.toString())
                        .child("quantity")
                        .setValue(oldQuantity - product.quantity)
                }
                .addOnFailureListener {
                    Log.e("RealtimeDatabase", it.toString())
                }
        }
    }

    fun updateSellPriceOfProduct(productItem: ProductItem, newValue: Float) {
        productItem.sell_price = newValue
        perfumesListReference
            .child(productItem.id.toString())
            .child("sell_price")
            .setValue(newValue)
    }
}