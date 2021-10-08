package com.example.commentsold.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

typealias Predicate = (value: String?) -> Boolean

class FormLiveDataValidator(private val liveData: LiveData<String>) {
    private val validationRules = mutableListOf<Predicate>()
    private val errorMessages = mutableListOf<String>()

    var error = MutableLiveData<String?>()

    // Check if the liveData value matches the error condition set in the
    // validation rule predicate.
    // The livedata value will be valid when its value does NOT match
    // an error condition set in the predicate
    fun isValid(): Boolean {
        for (i in 0 until validationRules.size) {
            if (validationRules[i](liveData.value)) {
                emitErrorMessage(errorMessages[i])
                return false
            }
        }

        emitErrorMessage(null)
        return true
    }

    //Emit error messages
    private fun emitErrorMessage(messageRes: String?) {
        error.value = messageRes
    }

    //Each form adds its own validation rules
    fun addRule(errorMsg: String, predicate: Predicate) {
        validationRules.add(predicate)
        errorMessages.add(errorMsg)
    }
}
