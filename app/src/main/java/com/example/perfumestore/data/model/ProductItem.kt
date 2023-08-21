package com.example.perfumestore.data.model

data class ProductItem(
    val id: Int,
    val imageId: Int,
    val name: String,
    val buy_price: Float,
    val sell_price: Float,
    val quantity: Int
)
