package com.twendev.vulpes.lagopus.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoadingUiState(
    val loading: LoadingStatus = LoadingStatus.Full
) {
    val isLoading : Boolean
        get() = loading != LoadingStatus.None
}

enum class LoadingStatus {
    None,
    Lazy,
    Full
}

abstract class LoadableViewModel : ViewModel()  {
    private val _uiState = MutableStateFlow(LoadingUiState())
    val uiState: StateFlow<LoadingUiState> = _uiState.asStateFlow()

    protected fun suspendActionWithLoading(status: LoadingStatus = LoadingStatus.Full, action: suspend () -> Unit) {
        setLoadingState(status)
        viewModelScope.launch {
            action()
            setLoadingState(LoadingStatus.None)
        }
    }
    protected fun suspendAction(action: suspend () -> Unit) {
        viewModelScope.launch {
            action()
        }
    }
    private fun setLoadingState(state: LoadingStatus) {
        _uiState.update { it.copy(loading = state) }
    }
}