package com.example.commentsold.data.network

import com.example.commentsold.data.network.model.*
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
    suspend fun getProduct(): Response<Product>

    @GET("styles")
    suspend fun getStyles(): Response<Styles>

    @POST("product")
    suspend fun addProduct(
       @Body product: CreateProduct
    ): Response<CreateProductResponse>
}