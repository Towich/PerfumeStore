package com.example.perfumestore

import com.example.perfumestore.data.database.RealtimeDatabase
import com.example.perfumestore.data.model.ProductItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainRepository {

    private val realtimeDatabase: RealtimeDatabase = RealtimeDatabase()

    fun getDatabase(): FirebaseDatabase = realtimeDatabase.getDatabase()
    fun getPerfumesListReference(): DatabaseReference = realtimeDatabase.getPerfumesListReference()
    fun updateQuantityOfProduct(productItem: ProductItem, newQuantity: Int){
        realtimeDatabase.updateQuantityOfProduct(productItem, newQuantity)
    }
    fun updateSellPriceOfProduct(productItem: ProductItem, newValue: Float) {
        realtimeDatabase.updateSellPriceOfProduct(productItem, newValue)
    }
}