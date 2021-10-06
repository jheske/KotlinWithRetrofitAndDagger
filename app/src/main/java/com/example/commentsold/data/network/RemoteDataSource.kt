package com.example.commentsold.data.network

import com.example.commentsold.utils.encode
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun login(username: String, password: String) = apiService.getStatus("$username:$password".encode())
    suspend fun getProduct() = apiService.getProduct()
    suspend fun getStyles() = apiService.getStyles()
}