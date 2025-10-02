package com.example.news.presentation.view_model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.news.state.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailNewsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // Mengambil title dan url dari argumen navigasi
    private val _title = MutableStateFlow(savedStateHandle.get<String>("title") ?: "Detail Berita")
    val title = _title.asStateFlow()

    private val _url = MutableStateFlow(savedStateHandle.get<String>("url") ?: "")
    val url = _url.asStateFlow()

    private val _detailViewState = MutableStateFlow<ViewState<Boolean>>(ViewState.Loading)
    val detailViewState = _detailViewState.asStateFlow()


    fun onPageStarted() {
        _detailViewState.value = ViewState.Loading
    }

    fun onPageFinished() {
        _detailViewState.value = ViewState.Success(true)
    }

    fun onPageError(errorMessage: String) {
        _detailViewState.value = ViewState.Error(errorMessage)
    }
}