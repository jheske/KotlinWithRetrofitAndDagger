package com.example.commentsold.ui.addproduct

import android.util.Log
import androidx.lifecycle.*
import com.example.commentsold.data.repository.Repository
import com.example.commentsold.ui.common.FormLiveDataValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _productAddedSuccess = MutableLiveData<Boolean>()
    val productAddedSuccess: LiveData<Boolean> = _productAddedSuccess

    val _stylesList = MutableLiveData<List<String>>()
    val stylesList: LiveData<List<String>> = _stylesList

    val productName = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val brand = MutableLiveData<String>()
    val style = MutableLiveData<String>()
    val price = MutableLiveData<String>()
    var productId: Int? = null

    /**
     * Validators for LiveData fields.
     * In a production app the rules would likely be more robust. For now, just
     * require non-empty fields.
     */
    val productNameValidator = FormLiveDataValidator(productName).apply {
        //Whenever the condition of the predicate is true, the error message should be emitted
        addRule("Product Name is required") {
            it.isNullOrBlank()
        }
    }
    val descriptionValidator = FormLiveDataValidator(description).apply {
        //Whenever the condition of the predicate is true, the error message should be emitted
        addRule("Description is required") {
            it.isNullOrBlank()
        }
    }
    val brandValidator = FormLiveDataValidator(brand).apply {
        //Whenever the condition of the predicate is true, the error message should be emitted
        addRule("Brand is required") {
            it.isNullOrBlank()
        }
    }
    val styleValidator = FormLiveDataValidator(style).apply {
        //Whenever the condition of the predicate is true, the error message should be emitted
        addRule("Style is required") {
            it.isNullOrBlank()
        }
    }
    val priceValidator = FormLiveDataValidator(price).apply {
        //Whenever the condition of the predicate is true, the error message should be emitted
        addRule("Price is required") {
            it.isNullOrBlank()
        }
    }

    /// isAddProductFormValidMediator calls these whenever form field values change
    fun validateForm() {
        val validators = listOf(
            productNameValidator,
            descriptionValidator,
            brandValidator,
            styleValidator,
            priceValidator
        )
        val validatorResolver = LiveDataValidatorResolver(validators)
        isAddProductFormValid.value = validatorResolver.isValid()
    }

    /**
     * isAddProductFormValid Mediator allows form to update the error state of form fields
     * and the enabled state of the SAVE button as the form data changes.
     */
    val isAddProductFormValid = MediatorLiveData<Boolean>()

    init {
        isAddProductFormValid.value = false
        isAddProductFormValid.apply {
            addSource(productName) { validateForm() }
            addSource(description) { validateForm() }
            addSource(brand) { validateForm() }
            addSource(style) { validateForm() }
            addSource(price) { validateForm() }
        }
    }

    fun addProduct() = viewModelScope.launch {
        // Field validation prevents empty fields.
        repository.addProduct(
            productName.value ?: "product name",
            description.value ?: "Product description",
            style.value ?: "style",
            brand.value ?: "brand",
            price.value ?: "0"
        )
            .collect {
                _productAddedSuccess.value = true
            }
    }

    fun updateProduct() = viewModelScope.launch {
        // Field validation prevents empty fields.
        repository.updateProduct(
            productId ?: 0,
            productName.value ?: "product name",
            description.value ?: "Product description",
            style.value ?: "style",
            brand.value ?: "brand",
            price.value ?: "0",
            productId ?: 0
        )
            .collect {
                _productAddedSuccess.value = true
            }
    }

    fun getProduct(id: Int) =
        viewModelScope.launch {
            repository.getProduct(id)
                .collect {
                    it.data?.let { data ->
                        productName.value = data.product.product_name
                        description.value = data.product.description
                        brand.value = data.product.brand
                        style.value = data.product.style
                        price.value = data.product.shippingPriceString()
                        productId = data.product.id
                    }
                }
        }

    fun getStyles() =
        viewModelScope.launch {
            repository.getStyles()
                .collect {
                    _stylesList.value = it.data?.styles ?: listOf()
                }
        }
}