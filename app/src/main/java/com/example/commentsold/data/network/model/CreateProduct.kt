package com.example.commentsold.data.network.model

data class CreateProduct(
    val product_name: String,
    val description: String,
    val style: String,
    val brand: String,
    val shipping_price_cents: Int,
    val url: Int?=null,
)
