package com.example.perfumestore

import androidx.lifecycle.ViewModel
import com.example.perfumestore.data.model.ProductItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainViewModel : ViewModel() {
    val repository: MainRepository = MainRepository()

    var currentProduct: ProductItem? = null

    fun getDatabase(): FirebaseDatabase = repository.getDatabase()
    fun getPerfumesListReference(): DatabaseReference = repository.getPerfumesListReference()

    fun updateQuantityOfProduct(productItem: ProductItem, newQuantity: Int) {
        repository.updateQuantityOfProduct(productItem, newQuantity)
    }

    fun updateSellPriceOfProduct(productItem: ProductItem, newSellPrice: Float) {
        repository.updateSellPriceOfProduct(productItem, newSellPrice)
    }
}