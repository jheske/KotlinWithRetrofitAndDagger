package com.example.commentsold.data.network.model

data class ProductsPage(
    val count: Int,
    val products: List<Product>,
    val total: Int
)