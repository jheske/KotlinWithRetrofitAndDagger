package com.example.commentsold.ui.addproduct

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commentsold.R
import com.example.commentsold.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _addProductForm = MutableLiveData<AddProductFormState>()
    val addProductFormState: LiveData<AddProductFormState> = _addProductForm

    fun formDataChanged(
        productName: String, description: String, style: String,
        brand: String, price: String
    ) {
        if (!isProductNameValid(productName)) {
            _addProductForm.value =
                AddProductFormState(productIdError = R.string.invalid_product_id)
        } else if (!isBrandValid(brand)) {
            _addProductForm.value = AddProductFormState(brandError = R.string.invalid_brand)
        } else if (!isStyleValid(style)) {
            _addProductForm.value = AddProductFormState(styleError = R.string.invalid_style)
        } else if (!isPriceValid(price)) {
            _addProductForm.value = AddProductFormState(priceError = R.string.invalid_price)
        } else if (!isDescriptionValid(description)) {
            _addProductForm.value =
                AddProductFormState(descriptionError = R.string.invalid_description)
        } else {
            _addProductForm.value = AddProductFormState(isDataValid = true)
        }
    }

    fun addProduct(
        productName: String, description: String,
        style: String, brand: String, price: String,
    ) =
        viewModelScope.launch {
            repository.addProduct(productName, description, style, brand, price)
                .collect {
                    Log.d("viewmodel", "Product created")
                }
        }

    /**
     * Rudimentary form validation checks mostly for empty fields.
     * Methods can be enhanced to include more rigorous validation,
     * eg., check productType, brand, etc against lists of valid values.
     */

    private fun isProductNameValid(productName: String): Boolean {
        return productName.isNotEmpty()
    }

    private fun isDescriptionValid(description: String): Boolean {
        return description.isNotEmpty()
    }

    private fun isStyleValid(style: String): Boolean {
        return style.isNotEmpty()
    }

    private fun isBrandValid(brand: String): Boolean {
        return brand.isNotEmpty()
    }

    private fun isPriceValid(price: String): Boolean {
        return price.isNotEmpty()
    }
}