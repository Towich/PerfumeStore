package com.example.perfumestore.data.database

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

    fun updateSellPriceOfProduct(productItem: ProductItem, newValue: Float) {
        productItem.sell_price = newValue
        perfumesListReference
            .child(productItem.id.toString())
            .child("sell_price")
            .setValue(newValue)
    }
}