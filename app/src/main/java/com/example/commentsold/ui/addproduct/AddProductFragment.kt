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
import androidx.navigation.fragment.navArgs
import com.example.commentsold.ui.productdetails.ProductDetailsFragmentArgs

@AndroidEntryPoint
class AddProductFragment : Fragment() {
    companion object {
        const val TAG = "AddProductFragment"
    }

    private val viewModel by viewModels<AddProductViewModel>()
    private val args: AddProductFragmentArgs by navArgs()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.product?.let {
            viewModel.getProduct(it.id)
        }
    }

    private fun setupObservers() {
        viewModel.productAddedSuccess.observe(viewLifecycleOwner, Observer {
            requireActivity().onBackPressed()
        })
    }

    private fun setupClickListeners() {
        binding.saveButton.setOnClickListener {
            args.product?.let {
                viewModel.addProduct()
            } ?: run {
                viewModel.updateProduct()
            }
        }
        binding.cancelButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}