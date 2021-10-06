package com.example.commentsold.ui.test

import android.view.View
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import com.example.commentsold.data.network.model.Product

data class ListViewState(
    val page: PagingData<Product>? = null,
    val adapterList: List<Product> = emptyList(),
    val errorMessageResource: Int? = null,
    val errorMessage: String? = null,
    val loadingStateVisibility: Int? = View.GONE,
    val errorVisibility: Int? = View.GONE
): BaseViewState

sealed class ViewEffect: BaseViewEffect {
    data class TransitionToScreen(val product: Product) : ViewEffect()
}

sealed class Event: BaseEvent {
    object SwipeToRefreshEvent: Event()
    data class LoadState(val state: CombinedLoadStates): Event()
    data class ListItemClicked(val item: Product): Event()
    // Suspended
    object ScreenLoad: Event()
}

sealed class Result: BaseResult {
    data class Error(val errorMessage: String?): Result()
    data class Content(val content: PagingData<Product>): Result()
    //data class ItemClickedResult(val item: Photo, val sharedElement: View) : Result()
}