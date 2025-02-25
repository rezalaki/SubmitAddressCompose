package com.rezalaki.registeraddresscompose.ui.screen.mapScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.rezalaki.registeraddresscompose.data.ApiHandlerState
import com.rezalaki.registeraddresscompose.data.model.submitAddress.SubmitAddressRequestBody
import com.rezalaki.registeraddresscompose.data.repository.AddressRepository
import com.rezalaki.registeraddresscompose.utils.Constants
import com.rezalaki.registeraddresscompose.utils.NetworkChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


sealed interface MapUiState {
    class ShowError(val errorsMessage: List<String>) : MapUiState
    data object Loading : MapUiState
    class SubmitFailed(val errors: List<String>) : MapUiState
    data class SubmitSuccess(val message:String) : MapUiState
    data object Idle : MapUiState
}


class MapViewModel(
    private val formRequestBody: SubmitAddressRequestBody,
    private val networkChecker: NetworkChecker,
    private val addressRepository: AddressRepository
) : ViewModel() {

    private val _uiState = Channel<MapUiState>()
    val uiState = _uiState.receiveAsFlow()

    fun getLatLng() = formRequestBody.getLatLng()

    fun setLatLng(latLng: LatLng) {
        formRequestBody.apply {
            lat = latLng.latitude
            lng = latLng.longitude
        }
    }

    fun submit() = viewModelScope.launch(Dispatchers.IO) {
        if (formRequestBody.lat == Constants.DEFAULT_LAT || formRequestBody.lng == Constants.DEFAULT_LNG) {
            _uiState.send(
                MapUiState.ShowError(
                    listOf("لطفا لوکیشن خود را انتخاب کنید")
                )
            )
            return@launch
        }

        if (networkChecker.isNetworkAvailable().not()) {
            _uiState.send(
                MapUiState.ShowError(
                    listOf("اینترنت خود را چک کنید")
                )
            )
            return@launch
        }

        addressRepository.submitForm(formRequestBody).collectLatest {
            val ui = when (it.state) {
                ApiHandlerState.SUCCESS -> MapUiState.SubmitSuccess("آدرس شما باموفقیت ثبت شد")
                ApiHandlerState.FAILED ->
                    MapUiState.SubmitFailed(
                        listOf(it.errorMessage.orEmpty())
                    )

                ApiHandlerState.LOADING -> MapUiState.Loading
            }
            _uiState.send(ui)
        }
    }

}