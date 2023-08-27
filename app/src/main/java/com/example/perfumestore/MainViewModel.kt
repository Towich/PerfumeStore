package com.example.perfumestore

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumestore.data.model.ProductItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StreamDownloadTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.concurrent.Flow

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MainRepository = MainRepository()

    var currentProduct: ProductItem? = null
    var fromCart: Boolean = false

    fun getItemsInCart(): MutableList<ProductItem> = repository.getItemsInCart()
    fun setItemsInCart(newList: MutableList<ProductItem>) {
        repository.setItemsInCart(newList)
    }

    fun clearItemsInCart() {
        repository.clearItemsInCart()
    }

    fun getDatabase(): FirebaseDatabase = repository.getDatabase()
    fun getPerfumesListReference(): DatabaseReference = repository.getPerfumesListReference()

    fun updateQuantityOfProduct(productItem: ProductItem, newQuantity: Int) {
        repository.updateQuantityOfProduct(productItem, newQuantity)
    }

    fun increaseQuantityInCart(productItem: ProductItem, updateUI: () -> Unit) {
        repository.increaseQuantityInCart(productItem, updateUI, getApplication())
    }

    fun updateQuantityOfProducts() {
        repository.updateQuantityOfProducts(repository.getItemsInCart())
    }

    fun updateSellPriceOfProduct(productItem: ProductItem, newSellPrice: Float) {
        repository.updateSellPriceOfProduct(productItem, newSellPrice)
    }

    fun addToCart(productItem: ProductItem) {
        repository.addToCart(productItem, getApplication())
    }

    fun getTotalCartSum(): Float = repository.getTotalCartSum()

    fun sendMessageNewOrderToTelegram() {
        var orderText: String =
            "У Вас новый заказ! Стоимость: " + String.format("%.2f", getTotalCartSum()) + " руб."
        for (item in repository.getItemsInCart()) {
            orderText += "\n~ ${item.producer} ${item.name}, ${item.volume} мл, ${item.quantity} шт."
        }
        sendMessageToTelegram(orderText)
    }

    private fun sendMessageToTelegram(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendMessageToTelegram(message)
        }
    }

    fun downloadImageFromStorage(it: StreamDownloadTask.TaskSnapshot): Bitmap? {
        var bitmap: Bitmap? = null

        viewModelScope.launch(Dispatchers.IO) {
            val job = async { BitmapFactory.decodeStream(it.stream) }
            bitmap = job.await()
        }

        return bitmap
    }
}