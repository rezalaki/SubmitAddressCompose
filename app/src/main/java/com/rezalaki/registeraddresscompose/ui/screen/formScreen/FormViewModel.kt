package com.rezalaki.registeraddresscompose.ui.screen.formScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezalaki.registeraddresscompose.data.model.submitAddress.SubmitAddressRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


sealed class FormUiState {
    class ShowErrors(val errors: List<String>) : FormUiState()
    data class GoNextScreen(val formBody: SubmitAddressRequestBody) : FormUiState()
    data object Idle : FormUiState()
}

class FormViewModel : ViewModel() {
    var formBody = SubmitAddressRequestBody()

    private val _uiState = Channel<FormUiState>(Channel.BUFFERED)
    val uiState = _uiState.receiveAsFlow()

    fun submit() = viewModelScope.launch(Dispatchers.IO) {
        val errors = formBody.validate()
        if (errors.isEmpty()) {
            _uiState.send(FormUiState.GoNextScreen(formBody))
        }
        else {
            _uiState.send(FormUiState.ShowErrors(errors))
        }
    }

}