package com.example.commentsold.ui.products

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.commentsold.data.network.model.Product
import com.example.commentsold.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    companion object {
        const val TAG = "ProductListViewModel"
    }

    val _deleteError = MutableLiveData<Int>()
    val deleteError: LiveData<Int> = _deleteError

    val _deleted = MutableLiveData<Boolean>()
    val deleted: LiveData<Boolean> = _deleted

    fun getProducts(): Flow<PagingData<Product>> {
        return repository.getProducts()
            .map { pagingData ->
               pagingData
            }
            .cachedIn(viewModelScope)
    }

    fun deleteProduct(productId: Int) = viewModelScope.launch {
        repository.deleteProduct(productId)
            .collect {
                // TODO this is NOT the correct way to do this. The error should
                // originate in the OkHttp3 Interceptor, which should throw an Exception and
                // flow the error back to here. I was not entirely sure how
                // implement this and ran out of time to research the solution.
                it.data?.let {
                    _deleted.value = true
                } ?: run {
                    _deleteError.value = productId
                }
            }
    }
}
