package com.example.commentsold.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.commentsold.data.network.*
import com.example.commentsold.data.network.model.BaseApiResponse
import com.example.commentsold.data.network.model.Product
import com.example.commentsold.data.network.model.ProductsPage
import com.example.commentsold.data.network.model.StatusResponse
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

//    // in-memory cache of the loggedInUser object
//    var user: LoggedInUser? = null
//        private set
//
//    val isLoggedIn: Boolean
//        get() = user != null
//
//    init {
//        // If user credentials will be cached in local storage, it is recommended it be encrypted
//        // @see https://developer.android.com/training/articles/keystore
//        user = null
//    }

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

//    fun getProducts(): Flow<PagingData<Product>> = Pager(
//        config = PagingConfig(pageSize = 20, prefetchDistance = 2),
//        pagingSourceFactory = {
//            ProductsPagingDataSource(apiService)
//        }
//    ).flow

//    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
//        this.user = loggedInUser
//        // sessionManager.saveAuthToken(loggedInUser.token)
//        // If user credentials will be cached in local storage, it is recommended it be encrypted
//        // @see https://developer.android.com/training/articles/keystore
//    }
}