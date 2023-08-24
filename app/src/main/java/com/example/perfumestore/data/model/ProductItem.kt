package com.example.perfumestore.data.model

data class ProductItem(
    val id: Int = 0,
    val imageId: Int = 0,
    val name: String = "",
    val producer: String = "",
    val buy_price: Float = 0f,
    val sell_price: Float = 0f,
    val quantity: Int = 0,
    val volume: Int = 0,
    val description: String = ""
)
