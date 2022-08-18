package com.sevdotdev.wowson.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevdotdev.data.repository.WowRepository
import com.sevdotdev.domain.model.WowMetaData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WowListViewModel @Inject constructor(private val repository: WowRepository) : ViewModel() {
    private val _wowListStateFlow: MutableStateFlow<List<WowMetaData>?> = MutableStateFlow(null)
    val wowListStateFlow: StateFlow<List<WowMetaData>?> = _wowListStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getWowsData().onEach { result ->
                result.fold(
                    onSuccess = {
                        _wowListStateFlow.value = it
                    },
                    onFailure = {
                        Log.d(this@WowListViewModel::class.simpleName, it.stackTraceToString())
                    }
                )
            }.collect()
        }
    }
}