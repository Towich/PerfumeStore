package com.example.perfumestore

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.example.perfumestore.data.database.RealtimeDatabase
import com.example.perfumestore.data.model.ProductItem
import com.example.perfumestore.data.source.LocalPerfumes
import com.example.perfumestore.data.source.TelegramApi
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.ktor.client.*

class MainRepository {

    private var itemsInCart: MutableList<ProductItem> = mutableListOf()
    private val realtimeDatabase: RealtimeDatabase = RealtimeDatabase()
    private val telegramApi: TelegramApi = TelegramApi(HttpClient())
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getItemsInCart(): MutableList<ProductItem> = itemsInCart

    fun setItemsInCart(newList: MutableList<ProductItem>) {
        itemsInCart = newList
    }
    fun clearItemsInCart(){
        itemsInCart = mutableListOf()
    }

    fun getDatabase(): FirebaseDatabase = realtimeDatabase.getDatabase()
    fun getPerfumesListReference(): DatabaseReference = realtimeDatabase.getPerfumesListReference()
    fun updateQuantityOfProduct(productItem: ProductItem, newQuantity: Int) {
        realtimeDatabase.updateQuantityOfProduct(productItem, newQuantity)
    }

    fun increaseQuantityInCart(productItem: ProductItem, updateUI: () -> Unit, application: Application){
        realtimeDatabase.getPerfumesListReference().child(productItem.id.toString())
            .child("quantity").get()
            .addOnSuccessListener {
                val quantityAtShop: Int = (it.value as Long).toInt()
                if(productItem.quantity < quantityAtShop){
                    updateUI()
                }
                else{
                    Toast.makeText(application, "На складе больше нет!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Log.e("MainRepository", "Error in getting quantity from database! (${productItem.name})\n" + it.toString())
            }
    }

    fun updateQuantityOfProducts(productItems: List<ProductItem>) {
        realtimeDatabase.updateQuantityOfProducts(productItems)
    }

    fun updateSellPriceOfProduct(productItem: ProductItem, newValue: Float) {
        realtimeDatabase.updateSellPriceOfProduct(productItem, newValue)
    }

    suspend fun sendMessageToTelegram(message: String) {
        telegramApi.sendMessage(message)
    }

    fun getTotalCartSum(): Float {
        var sum = 0f

        for (item in itemsInCart) {
            sum += item.sell_price * item.quantity
        }

        return sum
    }

    fun addToCart(productItem: ProductItem, application: Application) {
        realtimeDatabase.getPerfumesListReference().child(productItem.id.toString())
            .child("quantity").get()
            .addOnSuccessListener {
                val quantityAtShop: Int = (it.value as Long).toInt()
                Log.i("MainRepository", productItem.name + ": " + quantityAtShop.toString())
                var productInCartIndex: Int? = null


                for (i in 0 until itemsInCart.size) {
                    if (itemsInCart[i].id == productItem.id) {
                        productInCartIndex = i
                        break
                    }
                }

                // If this product is in cart
                if (productInCartIndex != null) {

                    if (itemsInCart[productInCartIndex].quantity >= quantityAtShop) {
                        Toast.makeText(
                            application,
                            "На складе больше нет!",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@addOnSuccessListener
                    }

                    itemsInCart[productInCartIndex].quantity++

                    Toast.makeText(
                        application,
                        "Добавлен ${itemsInCart[productInCartIndex].name}! В корзине: ${itemsInCart[productInCartIndex].quantity} шт.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    if (quantityAtShop < 1) {
                        Toast.makeText(
                            application,
                            "Нет в наличии",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@addOnSuccessListener
                    }

                    productItem.quantity = 1
                    itemsInCart.add(productItem)

                    Toast.makeText(
                        application,
                        "Добавлен ${productItem.name} в корзину!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {
                Log.e("RealtimeDatabase", "Error in getting the quantity of ${productItem.name}!")
            }
    }

    // Account functions
    fun isLoggedIntoAccount(): Boolean = mAuth.currentUser != null

    fun registerViaEmailPassword(email: String, password: String, onCompleted: (task: Task<AuthResult>) -> Unit){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            onCompleted(it)
        }
    }

    fun signInViaEmailPassword(email: String, password: String, onCompleted: (task: Task<AuthResult>) -> Unit){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            onCompleted(it)
        }
    }

    fun getNameAccount(): String = mAuth.currentUser?.displayName ?: "null"

    fun logOutFromAccount(){
//        mAuth.currentUser?.updateProfile(userProfileChangeRequest {
//            displayName = "Nikita Novichkov"
//        })

        mAuth.signOut()
    }
}