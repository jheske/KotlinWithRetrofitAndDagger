package com.example.commentsold.ui.addproduct

import androidx.lifecycle.MutableLiveData

/**
 * Data validation state of the login form.
 */
data class AddProductFormState(
    val productIdError: Int?=null,
    val brandError: Int?=null,
    val productNameError: Int?=null,
    val productTypeError: Int?=null,
    val styleError: Int?=null,
    val priceError: Int?=null,
    val descriptionError: Int?=null,
    val isDataValid: Boolean = false
)