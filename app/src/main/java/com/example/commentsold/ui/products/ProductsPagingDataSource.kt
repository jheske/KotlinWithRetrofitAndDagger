package com.example.commentsold.ui.products

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.commentsold.data.network.ApiService
import com.example.commentsold.data.network.model.Product

class ProductsPagingDataSource(
    private val service: ApiService
) : PagingSource<Int, Product>() {

    companion object {
        const val TAG = "ProductsPagingDataSource"
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val pageNumber = params.key ?: 1

        Log.d(TAG,"Loading products!")
        return try {
            val response = service.getProducts(pageNumber)
            val products = response.products

            Log.d("ProductsPagingDataSource", "Service -> getProducts: ${products?.size}")

            // 0 is the lowest page number. Return null to signify no more pages should
            // be loaded before it.
            val prevPageNumber = if (pageNumber > 0) pageNumber - 1 else null

            // Api is out of data when a page returns empty. When out of
            // data, so return null to signify no more pages should be loaded
            val nextPageNumber = if (products.isNotEmpty()) pageNumber + 1 else null

            LoadResult.Page(
                data = products,
                prevKey = prevPageNumber,
                nextKey = nextPageNumber
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

}