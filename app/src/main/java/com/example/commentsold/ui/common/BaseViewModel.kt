package com.example.commentsold.ui.common

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

abstract class BaseViewModel<ViewState : BaseViewState,  ViewAction : BaseViewEffect,
        Event: BaseEvent, Result: BaseResult>(initialState: ViewState) :  ViewModel() {

    companion object {
        const val TAG = "BaseViewModel"
    }

    internal val viewStateLD = MutableLiveData<ViewState>()
    private val viewEffectLD = MutableLiveData<ViewAction>()
    val viewState: LiveData<ViewState> get() = viewStateLD
    val viewEffects: LiveData<ViewAction> get() = viewEffectLD

    var loadJob: Job? = null

    fun onEvent(event: Event) {
        Log.d(TAG,"----- event ${event.javaClass.simpleName}")
        eventToResult(event)
    }

    suspend fun onSuspendedEvent(event: Event) {
        Log.d(TAG,"----- suspend event ${event.javaClass.simpleName}")
        suspendEventToResult(event)
    }

    abstract fun eventToResult(event: Event)

    abstract suspend fun suspendEventToResult(event: Event)

    abstract fun resultToViewState(result: Lce<Result>)
}

interface BaseViewState

interface BaseViewEffect

interface BaseEvent

interface BaseResult