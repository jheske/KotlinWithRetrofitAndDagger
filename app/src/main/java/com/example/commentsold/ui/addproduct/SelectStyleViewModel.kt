package com.example.commentsold.ui.addproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commentsold.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectStyleViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val _stylesList = MutableLiveData<List<String>>()
    val stylesList: LiveData<List<String>> = _stylesList

    fun getStyles() =
        viewModelScope.launch {
            repository.getStyles()
                .collect {
                    _stylesList.value = it.data?.styles ?: listOf()
                }
        }
}