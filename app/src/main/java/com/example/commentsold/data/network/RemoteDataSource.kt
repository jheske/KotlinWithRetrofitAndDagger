package com.example.commentsold.data.network

import android.util.Log
import com.example.commentsold.data.network.model.CreateProduct
import com.example.commentsold.data.network.model.Product
import com.example.commentsold.data.network.model.ProductResponse
import com.example.commentsold.utils.encode
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun login(username: String, password: String) =
        apiService.getStatus("$username:$password".encode())

    suspend fun getProduct(productId: Int): Response<ProductResponse> {
        return apiService.getProduct(productId)
    }

    suspend fun getStyles() = apiService.getStyles()

    suspend fun addProduct(
        product: CreateProduct
    ) = apiService.addProduct(product)

    suspend fun updateProduct(
        productId: Int,
        product: CreateProduct
    ) = apiService.updateProduct(productId, product)

    suspend fun deleteProduct(
        productId: Int,
    ) = apiService.deleteProduct(productId)
}