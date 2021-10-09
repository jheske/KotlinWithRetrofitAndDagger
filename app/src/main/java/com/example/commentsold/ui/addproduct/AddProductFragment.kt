package com.example.commentsold.ui.addproduct

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.commentsold.databinding.FragmentAddProductBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.commentsold.ui.productdetails.ProductDetailsFragmentArgs
import com.example.commentsold.utils.Keyboard

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

        viewModel.getStyles()
    }

    private fun setupObservers() {
        viewModel.productAddedSuccess.observe(viewLifecycleOwner, Observer {
            requireActivity().onBackPressed()
        })
        viewModel.stylesList.observe(viewLifecycleOwner, Observer {
            setupStylesAutocomplete(it)
            args.product?.let {product ->
                viewModel.getProduct(product.id)
            }
        })

    }

    private fun setupStylesAutocomplete(styles: List<String>) {
        var style: String=""

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_dropdown_item_1line,
            styles
        )

        binding.styleTextView.apply {
            setAdapter(adapter)
            // Auto complete threshold
            // The minimum number of characters to type to show the drop down
            threshold = 1

            onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    style = selectedItem
                    Keyboard.dismiss(this)
                }

            setOnDismissListener { }
            // Display the suggestion dropdown on focus
            onFocusChangeListener = View.OnFocusChangeListener { view, b ->
                if (b) {
                    showDropDown()
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.saveButton.setOnClickListener {
            args.product?.let {
                viewModel.updateProduct()
            } ?: run {
                viewModel.addProduct()
            }
        }
        binding.cancelButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}