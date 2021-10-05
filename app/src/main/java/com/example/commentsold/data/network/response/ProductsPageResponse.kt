package com.example.commentsold.data.network.response

data class ProductsPageResponse(
    val count: Int,
    val productResponses: List<ProductResponse>,
    val total: Int
)