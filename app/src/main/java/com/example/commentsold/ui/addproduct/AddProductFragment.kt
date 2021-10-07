package com.example.commentsold.ui.addproduct

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.commentsold.databinding.FragmentAddProductBinding
import com.example.commentsold.databinding.FragmentProductsBinding
import com.example.commentsold.ui.test.Event
import com.example.commentsold.ui.test.ListViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddProductFragment : Fragment() {
    companion object {
        const val TAG = "ProductListFragment"
    }

    private val viewModel by viewModels<AddProductViewModel>()

    private lateinit var binding: FragmentAddProductBinding

        override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddProductBinding.inflate(layoutInflater)
        return binding.root
    }
}