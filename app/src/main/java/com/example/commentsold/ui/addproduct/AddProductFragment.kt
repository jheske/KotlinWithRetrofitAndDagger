package com.example.commentsold.ui.addproduct

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.commentsold.databinding.FragmentAddProductBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.commentsold.R
import com.example.commentsold.ui.products.ProductsActivity

@AndroidEntryPoint
class AddProductFragment : Fragment() {
    companion object {
        const val TAG = "AddProductFragment"
    }

    private val viewModel by viewModels<AddProductViewModel>()
    private val args: AddProductFragmentArgs by navArgs()

    private lateinit var binding: FragmentAddProductBinding

    private var stylesList = arrayListOf<String>()

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
        args.product?.let {
            (activity as ProductsActivity).supportActionBar?.title = it.product_name
        } ?: run {
            (activity as ProductsActivity).supportActionBar?.title = getString(R.string.add_product)
        }
    }

    private fun setupObservers() {
        viewModel.productAddOrUpdate.observe(viewLifecycleOwner, Observer {
            requireActivity().onBackPressed()
        })
        viewModel.stylesList.observe(viewLifecycleOwner, {
            stylesList.addAll(it)
            args.product?.let { product ->
                viewModel.getProduct(product.id)
            }
        })
        binding.selectStyleButton.setOnClickListener {
            showSelectStyleDialog()
        }
    }

    private fun showSelectStyleDialog() {
        AlertDialog.Builder(requireActivity()).apply {
            setTitle("Select Genus")
            val listAdapter: ArrayAdapter<String> =
                ArrayAdapter(context, android.R.layout.select_dialog_item)
            stylesList.forEach {
                listAdapter.add(it)
            }
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            setAdapter(listAdapter) { _, which ->
                listAdapter.let {
                    binding.styleTextView.text = it.getItem(which)
                }
            }
            show()
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