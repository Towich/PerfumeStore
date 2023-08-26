package com.example.perfumestore

import com.example.perfumestore.data.database.RealtimeDatabase
import com.example.perfumestore.data.model.ProductItem
import com.example.perfumestore.data.source.TelegramApi
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.ktor.client.*

class MainRepository {

    private val realtimeDatabase: RealtimeDatabase = RealtimeDatabase()
    private val telegramApi: TelegramApi = TelegramApi(HttpClient())

    fun getDatabase(): FirebaseDatabase = realtimeDatabase.getDatabase()
    fun getPerfumesListReference(): DatabaseReference = realtimeDatabase.getPerfumesListReference()
    fun updateQuantityOfProduct(productItem: ProductItem, newQuantity: Int) {
        realtimeDatabase.updateQuantityOfProduct(productItem, newQuantity)
    }

    fun updateSellPriceOfProduct(productItem: ProductItem, newValue: Float) {
        realtimeDatabase.updateSellPriceOfProduct(productItem, newValue)
    }

    suspend fun sendMessageToTelegram(message: String) {
        telegramApi.sendMessage(message)
    }
}