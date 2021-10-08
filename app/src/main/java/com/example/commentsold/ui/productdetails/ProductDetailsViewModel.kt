package com.example.commentsold.ui.productdetails

import android.util.Log
import androidx.lifecycle.*
import com.example.commentsold.data.network.model.Product
import com.example.commentsold.data.repository.Repository
import com.example.commentsold.ui.common.FormLiveDataValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
     val product = MutableLiveData<Product>()
     val productLiveData: LiveData<Product> = product

     fun getProduct(productId: Int) =
        viewModelScope.launch {
            repository.getProduct(productId)
                .collect {
                    it.data?.let { data ->
                        product.value = data.product
                    }
                }
        }
}