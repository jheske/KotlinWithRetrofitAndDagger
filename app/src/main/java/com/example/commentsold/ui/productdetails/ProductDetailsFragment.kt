package com.example.commentsold.ui.productdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.commentsold.R
import com.example.commentsold.databinding.FragmentProductDetailsBinding
import com.example.commentsold.ui.products.ProductsActivity

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    companion object {
        const val TAG = "AddProductFragment"
    }

    private val viewModel by viewModels<ProductDetailsViewModel>()
    private val args: ProductDetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentProductDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as ProductsActivity).supportActionBar?.title = args.product.product_name

        viewModel.setupProduct(args.product)

        Glide.with(requireActivity())
            .load(args.product.getImageUrl(400))
            .into(binding.productImageView)
    }
}