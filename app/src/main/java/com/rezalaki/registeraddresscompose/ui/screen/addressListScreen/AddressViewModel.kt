package com.rezalaki.registeraddresscompose.ui.screen.addressListScreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezalaki.registeraddresscompose.data.ApiHandlerState
import com.rezalaki.registeraddresscompose.data.model.addressList.AddressItem
import com.rezalaki.registeraddresscompose.data.repository.AddressRepository
import com.rezalaki.registeraddresscompose.data.repository.AddressRepositoryImpl
import com.rezalaki.registeraddresscompose.ui.screen.formScreen.FormUiState
import com.rezalaki.registeraddresscompose.utils.NetworkChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


sealed interface AddressUiState {
    data class LoadSuccess(val addressList: List<AddressItem>) : AddressUiState
    data object Idle : AddressUiState
    data object EmptyList : AddressUiState
    data object Loading : AddressUiState
    data class NoInternet(val error:List<String>, val randomNumber: Int) : AddressUiState
    data class LoadFailed(val errorMessage: List<String>) : AddressUiState
}

class AddressViewModel(
    private val networkChecker: NetworkChecker,
    private val addressRepository: AddressRepository
) : ViewModel() {

    private val _uiState = Channel<AddressUiState>()
    val uiState = _uiState.receiveAsFlow()

    init {
        loadAddress()
    }

    fun loadAddress() = viewModelScope.launch(Dispatchers.IO) {
        if (networkChecker.isNetworkAvailable().not()) {
            _uiState.send(
                AddressUiState.NoInternet(
                    listOf("اینترنت خود را چک کنید"),
                    (1..500).random()
                )
            )
            return@launch
        }
        addressRepository.getAddressList().collect {
            when (it.state) {
                ApiHandlerState.SUCCESS -> {
                    val addressList = it.data as List<AddressItem>
                    if (addressList.isEmpty()) {
                        _uiState.send(AddressUiState.EmptyList)
                    } else {
                        val list = if (addressList.size > 15) {
                            addressList.take(15)
                        } else {
                            addressList
                        }
                        _uiState.send(AddressUiState.LoadSuccess(list))
                    }
                }

                ApiHandlerState.FAILED -> _uiState.send(
                    AddressUiState.LoadFailed(
                        listOf(it.errorMessage.orEmpty())
                    )
                )

                ApiHandlerState.LOADING -> _uiState.send(AddressUiState.Loading)
            }
        }
    }

}