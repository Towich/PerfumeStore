package com.example.perfumestore.data.model

data class ProductItem(
    val id: Int = 0,
    val imageUrl: String = "",
    val name: String = "",
    val producer: String = "",
    val buy_price: Float = 0f,
    var sell_price: Float = 0f,
    var quantity: Int = 0,
    val volume: Int = 0,
    val description: String = ""
)
