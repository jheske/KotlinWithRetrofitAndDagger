package com.example.commentsold.ui.products

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater

import android.view.ViewGroup

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.commentsold.R
import com.example.commentsold.data.network.model.Product
import com.example.commentsold.databinding.ListItemProductBinding
import com.example.commentsold.utils.Constants
import javax.inject.Inject

class ProductRecyclerAdapter @Inject constructor(
    val onProductClicked: (Product) -> Unit,
    val onEditProductClicked: (Product) -> Unit,
    val onDeleteProductClicked: (Product) -> Unit
) :
    PagingDataAdapter<Product, ProductRecyclerAdapter.ProductViewHolder>(ProductComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProductViewHolder(
            ListItemProductBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position)?.let { product ->
            holder.bind(product, holder.itemView.context)
            holder.itemView.setOnClickListener {
                onProductClicked(product)
            }
        }
    }

    inner class ProductViewHolder(
        private val binding: ListItemProductBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Product, context: Context) = with(binding) {
            binding.nameTextView.text = item.product_name
            val price = "$${item.shipping_price}"
            binding.priceTextView.text = price
            binding.editButtonHitArea.setOnClickListener {
                onEditProductClicked(item)
            }
            binding.deleteButtonHitArea.setOnClickListener {
                onDeleteProductClicked(item)
            }
            Glide.with(context)
                .load(item.getImageUrl())
                .into(binding.productImageView)
        }
    }

    object ProductComparator : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product) =
            oldItem == newItem
    }
}