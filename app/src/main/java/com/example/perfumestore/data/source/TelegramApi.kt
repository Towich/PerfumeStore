package com.example.perfumestore.data.source

import io.ktor.client.*
import io.ktor.client.request.*

class TelegramApi(
    private val httpClient: HttpClient,
    private val token: String = Confidentials.token,
    private val chatId: String = Confidentials.chatId
) {
    suspend fun sendMessage(text: String){
        val response = httpClient.post("https://api.telegram.org/bot$token/sendMessage"){
            parameter("chat_id", chatId)
            parameter("text", text)
        }
        println("sendMessage | CODE = ${response.status.value}")
    }
}