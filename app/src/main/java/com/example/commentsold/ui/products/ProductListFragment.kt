package com.example.commentsold.ui.products

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.commentsold.R
import com.example.commentsold.databinding.FragmentProductsBinding
import com.example.commentsold.ui.common.Event
import com.example.commentsold.ui.common.ListViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment : Fragment() {
    companion object {
        const val TAG = "ProductListFragment"
    }

    private val viewModel by viewModels<ProductListViewModel>()

    private lateinit var binding: FragmentProductsBinding
    lateinit var recyclerViewAdapter: ProductRecyclerAdapter

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
        if (savedInstanceState == null) {
            lifecycleScope.launch {
                viewModel.onSuspendedEvent(Event.ScreenLoad)
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
        recyclerViewAdapter = ProductRecyclerAdapter({
            findNavController().navigate(
                ProductListFragmentDirections.actionProductListFragmentToProductDetailsFragment(
                    it.id
                )
            )
        }, {
            findNavController().navigate(
                ProductListFragmentDirections.actionProductListFragmentToAddProductFragment(
                    it
                )
            )
        }, {
            viewModel.deleteProduct(it.id)
        })
        binding.productRecyclerView.adapter = recyclerViewAdapter

        recyclerViewAdapter.addLoadStateListener {
            Log.d(TAG, "loading state: $it")
            viewModel.onEvent(Event.LoadState(it))
        }
    }

    private fun setupObservers() {
        viewModel.obtainState.observe(viewLifecycleOwner, {
            Log.d(TAG, "observeViewState obtainState result: ${it.adapterList.size}")
            render(it)
        })
        viewModel.deleteError.observe (viewLifecycleOwner, {
            val message = String.format(getString(R.string.error_deleting_product),it)
            AlertDialog.Builder(requireActivity())
                .setTitle(R.string.delete_product)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .show()
        })
    }

    private fun render(state: ListViewState) {
        lifecycleScope.launch {
            state.page?.let { recyclerViewAdapter.submitData(it) }
        }
    }
}