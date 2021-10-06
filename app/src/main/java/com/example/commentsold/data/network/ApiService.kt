package com.example.commentsold.data.network

import com.example.commentsold.data.network.model.ProductsPage
import com.example.commentsold.data.network.model.StatusResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    @GET("status")
    suspend fun getStatus(
        @Header("Authorization") authorization: String
    ): Response<StatusResponse>

    @GET("products")
    suspend fun getProducts(
        @Query("page") page: Int=0,
        @Query("limit") limit: Int=10
    ):  ProductsPage

    @GET("product")
    suspend fun getProduct(): Response<StatusResponse>

    @GET("styles")
    suspend fun getStyles(): Response<StatusResponse>
}