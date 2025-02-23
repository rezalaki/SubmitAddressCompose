package com.rezalaki.registeraddresscompose.ui.screen.mapScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.rezalaki.registeraddresscompose.data.model.submitAddress.SubmitAddressRequestBody
import com.rezalaki.registeraddresscompose.ui.components.HeaderTop
import com.rezalaki.registeraddresscompose.ui.theme.colorBlackShadow
import com.rezalaki.registeraddresscompose.ui.theme.colorGreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun MapScreen(
    formRequestBody: SubmitAddressRequestBody,
    onBackPressed: () -> Unit,
    showSuccessMessage: (message: String) -> Unit,
    viewModel: MapViewModel = koinViewModel(parameters = { parametersOf(formRequestBody) }),
    goToAddressList: () -> Unit,
    showError: (errorMessageList: List<String>) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle(initialValue = MapUiState.Idle)
    var showLoadingSubmitButton by remember {
        mutableStateOf(false)
    }
    var startAnimation by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = startAnimation) {
        startAnimation = true
    }

    LaunchedEffect(key1 = uiState.value) {
        if (uiState.value != MapUiState.Loading) {
            showLoadingSubmitButton = false
        }
        when (uiState.value) {
            MapUiState.Idle -> {

            }

            MapUiState.Loading -> {
                showLoadingSubmitButton = true
            }

            is MapUiState.ShowError -> {
                val errorText = (uiState.value as MapUiState.ShowError).errorsMessage
                showError.invoke(errorText)
            }

            is MapUiState.SubmitFailed -> {
                val errorText = (uiState.value as MapUiState.SubmitFailed).errors
                showError.invoke(errorText)
            }

            is MapUiState.SubmitSuccess -> {
                val message = (uiState.value as MapUiState.SubmitSuccess).message
                showSuccessMessage.invoke(message)
                goToAddressList.invoke()
            }
        }
    }


    val coroutineScope = rememberCoroutineScope()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(viewModel.getLatLng(), 16f)
    }
    val selectedPointMarkerState = rememberMarkerState()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        HeaderTop(title = "موقعیت روی نقشه") {
            onBackPressed.invoke()
        }
        GoogleMap(modifier = Modifier
            .fillMaxSize()
            .padding(top = 56.dp),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false, zoomGesturesEnabled = true
            ),
            onMapLongClick = { point ->
                selectedPointMarkerState.position = point
                coroutineScope.launch {
                    viewModel.setLatLng(point)
                    cameraPositionState.animate(CameraUpdateFactory.newLatLng(viewModel.getLatLng()))
                }
            }) {
            Marker(
                state = selectedPointMarkerState
            )
        }
        AnimatedVisibility(
            visible = startAnimation,
            enter = fadeIn(animationSpec = tween(2_000))
        ) {
            Text(
                text = "موقعیت مورد نظر خود را روی نقشه مشخص کنید",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp)
                    .background(
                        color = colorBlackShadow
                    )
                    .padding(8.dp),
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(60.dp)
                .align(Alignment.BottomCenter)
                .padding(8.dp), colors = ButtonDefaults.buttonColors(
                containerColor = colorGreen
            ), shape = RoundedCornerShape(6.dp),
            onClick = {
                viewModel.submit()
            },
            enabled = showLoadingSubmitButton.not()
        ) {
            if (showLoadingSubmitButton) Text(text = "لطفا صبر کنید", fontSize = 18.sp)
            else Text(text = "تایید موقعیت", fontSize = 18.sp)
        }
    }
}


