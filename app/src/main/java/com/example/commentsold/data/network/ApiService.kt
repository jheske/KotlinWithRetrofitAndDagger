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

    @GET("product/{id}")
    suspend fun getProduct(
        @Path("id") productId: Int
    ): Response<ProductResponse>

    @GET("styles")
    suspend fun getStyles(): Response<Styles>

    @POST("product")
    suspend fun addProduct(
        @Body product: CreateProduct
    ): Response<CreateProductResponse>

    @DELETE("product/{id}")
    suspend fun deleteProduct(
        @Path("id") productId: Int
    ): Response<UpdateProductResponse>

//    @DELETE("product/{id}")
//    suspend fun deleteProduct(
//        @Path("id") productId: Int
//    ): Response<UpdateProductResponse>

    @PUT("product/{id}")
    suspend fun updateProduct(
        @Path("id") productId: Int,
        @Body product: CreateProduct
    ): Response<UpdateProductResponse>
}