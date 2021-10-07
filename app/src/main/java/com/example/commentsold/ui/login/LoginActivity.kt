package com.example.commentsold.ui.login

import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import com.example.commentsold.databinding.ActivityLoginBinding
import com.example.commentsold.data.network.NetworkResult
import com.example.commentsold.di.SessionManager
import com.example.commentsold.ui.products.ProductsActivity
import com.example.commentsold.utils.afterTextChanged
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    companion object {
        const val TAG = "LoginActivity"
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var loading: ProgressBar
    private val viewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindViews()
        setupObservers()
        setupTextListeners()
    }

    private fun setupTextListeners() {
        username.afterTextChanged {
            viewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                viewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        viewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                viewModel.login(username.text.toString(), password.text.toString())
            }
        }
    }

    private fun bindViews() {
        username = binding.username
        password = binding.password
        login = binding.login
        loading = binding.loading
    }

    private fun setupObservers() {
        viewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        viewModel.loginResult.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    loading.visibility = View.GONE
                    response.data?.let {
                        sessionManager.saveAuthToken(it.token)
                        startActivity(ProductsActivity.intent(this))
                    }
                }

                is NetworkResult.Error -> {
                    loading.visibility = View.GONE
                }
                is NetworkResult.Loading -> {
                }
            }
        }
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}
