package com.example.commentsold.data.network

import com.example.commentsold.data.network.model.CreateProduct
import com.example.commentsold.data.network.model.CreateProductResponse
import com.example.commentsold.data.network.model.ProductsPage
import com.example.commentsold.data.network.model.StatusResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("status")
    suspend fun getStatus(
        @Header("Authorization") authorization: String
    ): Response<StatusResponse>

    @GET("products")
    suspend fun getProducts(
        @Query("page") page: Int = 0,
        @Query("limit") limit: Int = 10
    ): ProductsPage

    @GET("product")
    suspend fun getProduct(): Response<StatusResponse>

    @GET("styles")
    suspend fun getStyles(): Response<StatusResponse>

    @POST("product")
    suspend fun addProduct(
       @Body product: CreateProduct
    ): Response<CreateProductResponse>
}