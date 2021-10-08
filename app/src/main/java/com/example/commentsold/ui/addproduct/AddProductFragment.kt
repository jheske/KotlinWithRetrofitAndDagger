package com.example.commentsold.ui.addproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.commentsold.databinding.FragmentAddProductBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.Observer

@AndroidEntryPoint
class AddProductFragment : Fragment() {
    companion object {
        const val TAG = "AddProductFragment"
    }

    private val viewModel by viewModels<AddProductViewModel>()

     private lateinit var binding: FragmentAddProductBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddProductBinding.inflate(layoutInflater)
        setupObservers()
        setupClickListeners()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun setupObservers() {
        viewModel.productAddedSuccess.observe(viewLifecycleOwner, Observer {
            requireActivity().onBackPressed()
        })
    }

    private fun setupClickListeners() {
        binding.saveButton.setOnClickListener {
            viewModel.addProduct()
        }
        binding.cancelButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}