package com.example.commentsold.ui.addproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.commentsold.databinding.FragmentAddProductBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.Observer
import com.example.commentsold.utils.afterTextChanged

@AndroidEntryPoint
class AddProductFragment : Fragment() {
    companion object {
        const val TAG = "AddProductFragment"
    }

    private val viewModel by viewModels<AddProductViewModel>()

    private lateinit var binding: FragmentAddProductBinding
    private lateinit var productName: com.google.android.material.textfield.TextInputEditText
    private lateinit var description: com.google.android.material.textfield.TextInputEditText
    private lateinit var style: com.google.android.material.textfield.TextInputEditText
    private lateinit var brand: com.google.android.material.textfield.TextInputEditText
    private lateinit var price: com.google.android.material.textfield.TextInputEditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddProductBinding.inflate(layoutInflater)
        bindViews()
        setupObservers()
        setupTextListeners()
        setupClickListeners()
        return binding.root
    }

    private fun setupTextListeners() {
        productName.afterTextChanged {
            onFormChanged()
        }
        brand.afterTextChanged {
            onFormChanged()
        }
        style.afterTextChanged {
            onFormChanged()
        }
        description.afterTextChanged {
            onFormChanged()
        }
        price.afterTextChanged {
            onFormChanged()
        }
    }

    private fun onFormChanged() {
        viewModel.formDataChanged(
            productName.text.toString(),
            description.text.toString(),
            style.text.toString(),
            brand.text.toString(),
            price.text.toString(),
        )
    }

    private fun setupObservers() {
        viewModel.addProductFormState.observe(viewLifecycleOwner, Observer {
            val addProductState = it ?: return@Observer

            saveButton.isEnabled = addProductState.isDataValid

            if (addProductState.productNameError != null) {
                productName.error = getString(addProductState.productNameError)
            }

            if (addProductState.descriptionError != null) {
                description.error = getString(addProductState.descriptionError)
            }

            if (addProductState.styleError != null) {
                style.error = getString(addProductState.styleError)
            }

            if (addProductState.brandError != null) {
                brand.error = getString(addProductState.brandError)
            }

            if (addProductState.priceError != null) {
                price.error = getString(addProductState.priceError)
            }
        })
    }

    private fun setupClickListeners() {
        saveButton.setOnClickListener {
            viewModel.addProduct(
                productName.text.toString(),
                description.text.toString(),
                style.text.toString(),
                brand.text.toString(),
                price.text.toString()
            )
        }
        cancelButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun bindViews() {
        saveButton = binding.saveButton
        cancelButton = binding.cancelButton
        productName = binding.productNameEditText
        description = binding.descriptionEditText
        brand = binding.brandEditText
        style = binding.styleEditText
        price = binding.priceEditText
    }
}