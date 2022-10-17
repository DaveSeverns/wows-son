package com.sevdotdev.wowson.screens.wowdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sevdotdev.data.repository.WowRepository
import com.sevdotdev.data.repository.WowRepositoryBaseImpl
import com.sevdotdev.domain.model.WowMetaData
import com.sevdotdev.wowson.screens.wowlist.WowListViewModel
import com.sevdotdev.wowson.ui.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias WowDetailsViewState = UiState<WowMetaData>

@HiltViewModel
class WowDetailsViewModel @Inject constructor(private val repository: WowRepository) : ViewModel() {
    private val _wowDataViewState: MutableStateFlow<WowDetailsViewState> =
        MutableStateFlow(UiState.Loading)
    val wowDataViewState: StateFlow<WowDetailsViewState> = _wowDataViewState.asStateFlow()

    suspend fun getWowData(wowId: String) = viewModelScope.launch {
        repository.getWowById(wowId)
            .collectLatest {
                it.fold(
                    onSuccess = { metaData ->
                        _wowDataViewState.value = UiState.Success(metaData)
                    },
                    onFailure = { throwable ->
                        _wowDataViewState.value = UiState.Error(exception = throwable)
                    }
                )
            }
    }
}