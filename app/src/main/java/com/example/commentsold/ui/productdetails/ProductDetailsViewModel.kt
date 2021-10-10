package com.example.commentsold.ui.productdetails

import androidx.lifecycle.*
import com.example.commentsold.data.network.model.Product
import com.example.commentsold.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
     val product = MutableLiveData<Product>()

    fun setupProduct(prod: Product) {
        product.value = prod
    }
}