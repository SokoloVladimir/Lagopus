package com.twendev.vulpes.lagopus.ui.viewmodel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twendev.vulpes.lagopus.ui.component.circleloading.CircleLoading
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
    private val _loadingUiState = MutableStateFlow(LoadingUiState())
    val loadingUiState: StateFlow<LoadingUiState> = _loadingUiState.asStateFlow()

    @Composable
    fun WithLoadable(content: @Composable () -> Unit) {
        val state = loadingUiState.collectAsState()

        Box {
            if (state.value.isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircleLoading()
                }
            }
            if (state.value.loading != LoadingStatus.Full) {
                content()
            }
        }
    }

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
        _loadingUiState.update { it.copy(loading = state) }
    }
}