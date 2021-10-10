package com.example.commentsold.ui.common

class LiveDataValidatorResolver(private val validators: List<FormLiveDataValidator>) {
    fun isValid(): Boolean {
        for (validator in validators) {
            if (!validator.isValid()) return false
        }
        return true
    }
}