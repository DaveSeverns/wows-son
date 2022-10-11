package com.sevdotdev.wowson.screens.wowlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevdotdev.data.repository.WowRepository
import com.sevdotdev.domain.model.WowMetaData
import com.sevdotdev.wowson.ui.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias WowListViewState = UiState<List<WowMetaData>>

@HiltViewModel
class WowListViewModel @Inject constructor(private val repository: WowRepository) : ViewModel() {
    private val _wowListStateFlow: MutableStateFlow<WowListViewState> =
        MutableStateFlow(UiState.Loading)
    val wowListStateFlow: StateFlow<WowListViewState> = _wowListStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getRandomWows().onEach { result ->
                result.fold(
                    onSuccess = {
                        _wowListStateFlow.value = UiState.Success(viewState = it)
                    },
                    onFailure = {
                        Log.d(this@WowListViewModel::class.simpleName, it.stackTraceToString())
                        _wowListStateFlow.value = UiState.Error(exception = it)
                    }
                )
            }.collect()
        }
    }
}