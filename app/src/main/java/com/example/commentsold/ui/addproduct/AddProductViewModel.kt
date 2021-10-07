package com.example.commentsold.ui.addproduct

import androidx.lifecycle.ViewModel
import com.example.commentsold.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

}