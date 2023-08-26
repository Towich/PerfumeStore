package com.example.perfumestore

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumestore.data.source.LocalPerfumes
import com.example.perfumestore.data.model.ProductItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MainRepository = MainRepository()

    var currentProduct: ProductItem? = null
    var itemsInCart: MutableList<ProductItem> =
        LocalPerfumes().createList() as MutableList<ProductItem>
    var fromCart: Boolean = false

    fun getDatabase(): FirebaseDatabase = repository.getDatabase()
    fun getPerfumesListReference(): DatabaseReference = repository.getPerfumesListReference()

    fun updateQuantityOfProduct(productItem: ProductItem, newQuantity: Int) {
        repository.updateQuantityOfProduct(productItem, newQuantity)
    }

    fun updateSellPriceOfProduct(productItem: ProductItem, newSellPrice: Float) {
        repository.updateSellPriceOfProduct(productItem, newSellPrice)
    }

    fun addToCart(productItem: ProductItem) {
        var productInCartIndex: Int? = null

        for (i in 0 until itemsInCart.size) {
            if (itemsInCart[i].id == productItem.id) {
                productInCartIndex = i
                break
            }
        }

        if (productInCartIndex != null) {
            itemsInCart[productInCartIndex].quantity++

            Toast.makeText(
                getApplication(),
                "Добавлен ${itemsInCart[productInCartIndex].name}! В корзине: ${itemsInCart[productInCartIndex].quantity} шт.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            productItem.quantity = 1
            itemsInCart.add(productItem)

            Toast.makeText(
                getApplication(),
                "Добавлен ${productItem.name} в корзину!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun getTotalCartSum(): Float {
        var sum = 0f

        for (item in itemsInCart) {
            sum += item.sell_price * item.quantity
        }

        return sum
    }

    fun sendMessageToTelegram(message: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendMessageToTelegram(message)
        }
    }
}