package com.example.commentsold.data.network

import com.example.commentsold.data.network.response.ProductsPageResponse
import com.example.commentsold.data.network.response.StatusResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {

    @GET("status")
    suspend fun getStatus(
        @Header("Authorization") authorization: String
    ): Response<StatusResponse>

    @GET("productResponses")
    suspend fun getProducts(
    ): Response<ProductsPageResponse>

    @GET("product")
    suspend fun getProduct(
    ): Response<StatusResponse>

    @GET("styles")
    suspend fun getStyles(
    ): Response<StatusResponse>
}