package com.example.commentsold.ui.products

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import com.example.commentsold.R
import com.example.commentsold.data.repository.Repository
import com.example.commentsold.ui.common.BaseViewModel
import com.example.commentsold.ui.common.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: Repository
) : BaseViewModel<ListViewState, ViewEffect, Event, Result>(ListViewState()) {

    companion object {
        const val TAG = "ProductListViewModel"
    }

    val _deleteError = MutableLiveData<Int>()
    val deleteError: LiveData<Int> = _deleteError

    private val viewAction = MutableLiveData<ViewEffect>()
    val obtainState: LiveData<ListViewState> = viewState

    private var currentViewState = ListViewState()
        set(value) {
            field = value
            viewStateLD.postValue(value)
        }

    private fun fetchData() {
        resultToViewState(Lce.Loading())
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getProducts()
                .cachedIn(viewModelScope)
                .onEach { flowData -> Log.d(TAG, "returned FLOW: $flowData") }
                .collect {results ->
                    resultToViewState(Lce.Content(Result.Content(results)))
                }
        }
    }


    fun deleteProduct(productId: Int) = viewModelScope.launch {
        repository.deleteProduct(productId)
            .collect {
                // TODO this is NOT the correct way to do this. The error should
                // originate in the OkHttp3 Interceptor, which should throw an Exception and
                // flow the error back to here. I was not entirely sure how
                // implement this and ran out of time to research the solution.
                it.data?.let {
                    getProducts()
                } ?: run {
                    _deleteError.value = productId
                }
            }
    }

    override fun eventToResult(event: Event) {
        when (event) {
            is Event.ListItemClicked -> viewAction.postValue(ViewEffect.TransitionToScreen(event.item))
            is Event.LoadState -> onLoadState(event.state)
        }
    }

    private fun onLoadState(state: CombinedLoadStates) {
        when (state.source.refresh) {
            is LoadState.Error -> {
                val errorState = state.source.append as? LoadState.Error
                    ?: state.source.prepend as? LoadState.Error
                    ?: state.append as? LoadState.Error
                    ?: state.prepend as? LoadState.Error
                errorState?.let {
                    resultToViewState(Lce.Error(Result.Error(errorMessage = errorState.error.localizedMessage)))
                }
            }
            is LoadState.Loading -> resultToViewState(Lce.Loading())
        }

    }

    override suspend fun suspendEventToResult(event: Event) {
        when (event) {
            is Event.ScreenLoad, Event.SwipeToRefreshEvent -> fetchData()
        }
    }

    override fun resultToViewState(result: Lce<Result>) {
        currentViewState = when (result) {
            //Loading state
            is Lce.Loading -> {
                currentViewState.copy(
                    loadingStateVisibility = View.VISIBLE,
                    errorVisibility = View.GONE
                )
            }
            //Content state
            is Lce.Content -> {
                when (result.packet) {
                    is Result.Content ->
                        currentViewState.copy(
                            page = result.packet.content,
                            loadingStateVisibility = View.GONE,
                            errorVisibility = View.GONE
                        )
                    else -> currentViewState.copy()
                }
            }
            //Error state
            is Lce.Error -> {
                when (result.packet) {
                    is Result.Error ->
                        currentViewState.copy(
                            errorVisibility = View.VISIBLE,
                            errorMessage = result.packet.errorMessage,
                            loadingStateVisibility = View.GONE
                        )
                    else -> currentViewState.copy()
                }
            }
        }
    }
}
