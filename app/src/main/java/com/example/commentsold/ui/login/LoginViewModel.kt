package com.example.commentsold.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.commentsold.data.repository.Repository
import com.example.commentsold.R
import com.example.commentsold.data.network.NetworkResult
import com.example.commentsold.data.network.model.ProductsPage
import com.example.commentsold.data.network.model.StatusResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult: MutableLiveData<NetworkResult<StatusResponse>> = MutableLiveData()
    val loginResult: LiveData<NetworkResult<StatusResponse>> = _loginResult

    private val _products: MutableLiveData<NetworkResult<ProductsPage>> = MutableLiveData()
    val products: LiveData<NetworkResult<ProductsPage>> = _products

    fun login(username: String, password: String) =
        viewModelScope.launch {
            repository.login(username, password).collect { values ->
                _loginResult.value = values
            }
        }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
