package com.example.commentsold.ui.products

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.commentsold.databinding.FragmentProductsBinding
import com.example.commentsold.ui.test.Event
import com.example.commentsold.ui.test.ListViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment : Fragment() {
    companion object {
        const val TAG = "ProductListFragment"
    }

    private val viewModel by viewModels<ProductsViewModel>()

    private lateinit var binding: FragmentProductsBinding
    val recyclerViewAdapter = ProductRecyclerAdapter()

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
        setupBinding()
        observeViewState()
        if (savedInstanceState == null) {
            lifecycleScope.launch {
                viewModel.onSuspendedEvent(Event.ScreenLoad)
            }
        }
    }

    private fun setupBinding() {
        binding.productRecyclerView.apply {
            layoutManager = GridLayoutManager(context,2)
            initAdapter()
        }
    }

    private fun initAdapter() {
        binding.productRecyclerView.adapter = recyclerViewAdapter

        recyclerViewAdapter.addLoadStateListener {
            Log.d(TAG, "loading state: ${it.toString()}")
            viewModel.onEvent(Event.LoadState(it))
        }
    }

    private fun observeViewState() {
        viewModel.obtainState.observe(viewLifecycleOwner, {
            Log.d(TAG, "observeViewState obtainState result: ${it.adapterList.size}")
            render(it)
        })
    }

//    private fun setScrollToTopWHenRefreshedFromNetwork() {
//        // Scroll to top when the list is refreshed from network.
//        lifecycleScope.launch {
//            recyclerViewAdapter.loadStateFlow
//                // Only emit when REFRESH LoadState for RemoteMediator changes.
//                .distinctUntilChangedBy { it.refresh }
//                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
//                .filter { it.refresh is LoadState.NotLoading }
//                .collect { binding.productRecyclerView.scrollToPosition(0) }
//        }
//    }

    private fun render(state: ListViewState) {
        //state.loadingStateVisibility?.let { binding.progressBar.visibility = it }
        lifecycleScope.launch {
            state.page?.let { recyclerViewAdapter.submitData(it) }
        }
    }
}