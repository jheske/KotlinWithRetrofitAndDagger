package com.example.commentsold.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.commentsold.data.network.*
import com.example.commentsold.data.network.model.*
import com.example.commentsold.di.SessionManager
import com.example.commentsold.ui.products.ProductsPagingDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

@ActivityRetainedScoped
class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val apiService: ApiService
) : BaseApiResponse() {

    @Inject
    lateinit var sessionManager: SessionManager

    suspend fun login(username: String, password: String): Flow<NetworkResult<StatusResponse>> {
        return flow {
            emit(safeApiCall {
                remoteDataSource.login(username, password)
            })
        }.map { response ->
            response.data?.let {
                sessionManager.saveAuthToken(it.token)
            }
            response
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getProduct(productId: Int): Flow<NetworkResult<ProductResponse>> {
        return flow {
            emit(safeApiCall {
                remoteDataSource.getProduct(productId)
            })
        }.map { response ->
            Log.d("Repo",response.data?.product?.product_name ?: "empty product name")
            response
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getStyles(): Flow<NetworkResult<Styles>> {
        return flow {
            emit(safeApiCall {
                remoteDataSource.getStyles()
            })
        }.map { response ->
            response
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addProduct(
        productName: String, description: String,
        style: String, brand: String, price: String
    ): Flow<NetworkResult<CreateProductResponse>> {
        return flow {
            emit(safeApiCall {
                remoteDataSource.addProduct(
                    CreateProduct(
                        productName,
                        description,
                        style,
                        brand,
                        price.toInt()
                    )
                )
            })
        }.map { response ->
            response
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateProduct(
        productId: Int, productName: String, description: String,
        style: String, brand: String, price: String,url: Int
    ): Flow<NetworkResult<UpdateProductResponse>> {
        return flow {
            emit(safeApiCall {
                remoteDataSource.updateProduct(
                    productId,
                    CreateProduct(
                        productName,
                        description,
                        style,
                        brand,
                        price.toInt(),
                        url
                    )
                )
            })
        }.map { response ->
            response
        }.flowOn(Dispatchers.IO)
    }

    suspend fun deleteProduct(productId: Int): Flow<NetworkResult<UpdateProductResponse>> {
        return flow {
            emit(safeApiCall {
                remoteDataSource.deleteProduct(productId=9999)
            })
        }.map { response ->
            response
        }.flowOn(Dispatchers.IO)
    }

    fun getProducts(): Flow<PagingData<Product>> {
        Log.d("Repository", "New page")
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ProductsPagingDataSource(apiService) }
        ).flow
    }
}