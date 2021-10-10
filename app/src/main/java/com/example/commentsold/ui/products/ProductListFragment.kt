package com.example.commentsold.ui.products

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.commentsold.R
import com.example.commentsold.databinding.FragmentProductsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment : Fragment() {
    companion object {
        const val TAG = "ProductListFragment"
    }

    private val viewModel by viewModels<ProductListViewModel>()

    private lateinit var binding: FragmentProductsBinding
    lateinit var pagingAdapter: ProductRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBindings()
        setupObservers()
        collectUiState()
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getProducts().collectLatest { products ->
                pagingAdapter.submitData(products)
            }
        }
    }

    private fun setupBindings() {
        binding.productRecyclerView.apply {
            initAdapter()
        }
        binding.addProductFab.setOnClickListener {
            findNavController().navigate(
                ProductListFragmentDirections.actionProductListFragmentToAddProductFragment(
                    null
                )
            )
        }
    }

    private fun initAdapter() {
        pagingAdapter = ProductRecyclerAdapter(
            onProductClicked = {
                findNavController().navigate(
                    ProductListFragmentDirections.actionProductListFragmentToProductDetailsFragment(
                        it
                    )
                )
            },
            onEditProductClicked = {
                findNavController().navigate(
                    ProductListFragmentDirections.actionProductListFragmentToAddProductFragment(
                        it
                    )
                )
            },
            onDeleteProductClicked =  {
                val message = String.format(getString(R.string.verify_delete_product, it.product_name))
                AlertDialog.Builder(requireActivity())
                    .setTitle(R.string.delete_product)
                    .setMessage(message)
                    .setCancelable(true)
                    .setPositiveButton(R.string.ok) { _, _ ->
                        viewModel.deleteProduct(it.id)
                    }
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.cancel()
                    }
                    .show()
            }
        )

        binding.productRecyclerView.adapter = pagingAdapter
    }

    private fun setupObservers() {
        viewModel.deleteError.observe(viewLifecycleOwner, {
            val message = String.format(getString(R.string.error_deleting_product), it)
            AlertDialog.Builder(requireActivity())
                .setTitle(R.string.delete_product_error)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .show()
        })
        viewModel.deleted.observe(viewLifecycleOwner,{
            pagingAdapter.refresh()
        })
    }
}