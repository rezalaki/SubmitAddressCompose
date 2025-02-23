package com.rezalaki.registeraddresscompose.ui.screen.addressListScreen

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rezalaki.registeraddresscompose.data.model.addressList.AddressItem
import com.rezalaki.registeraddresscompose.ui.components.HeaderTop
import com.rezalaki.registeraddresscompose.ui.theme.colorBackground
import com.rezalaki.registeraddresscompose.ui.theme.colorGreen
import org.koin.androidx.compose.koinViewModel


@Composable
fun AddressScreen(
    viewModel: AddressViewModel = koinViewModel(),
    backPressed: () -> Unit,
    showError: (errorMessageList: List<String>) -> Unit
) {
    val uiState by remember {
        viewModel.uiState
    }.collectAsStateWithLifecycle(AddressUiState.Idle)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorBackground)
    ) {
        HeaderTop(title = "لیست آدرس ها") {
            backPressed.invoke()
        }

        when (uiState) {
            AddressUiState.EmptyList -> {
                EmptyStateBox()
            }

            AddressUiState.Idle -> {
                viewModel.loadAddress()
            }

            is AddressUiState.LoadFailed -> {
                val error = (uiState as AddressUiState.LoadFailed).errorMessage
                showError.invoke(error)
                TryAgainBox {
                    viewModel.loadAddress()
                }
            }

            is AddressUiState.LoadSuccess -> {
                val addressList = (uiState as AddressUiState.LoadSuccess).addressList
                ListOfAddressBox(addressList)
            }

            AddressUiState.Loading -> {
                LoadingBox()
            }

            is AddressUiState.NoInternet -> {
                val error = (uiState as AddressUiState.NoInternet).error
                showError.invoke(error)
                TryAgainBox {
                    viewModel.loadAddress()
                }
            }
        }

    }
}


@Composable
fun LoadingBox() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun EmptyStateBox() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "دیتایی برای نمایش وجود ندارد", modifier = Modifier.padding(vertical = 16.dp))
    }
}


@Composable
fun TryAgainBox(
    buttonClicked: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "خطایی رخ داد", modifier = Modifier.padding(vertical = 16.dp))
            Button(
                onClick = { buttonClicked.invoke() }
            ) {
                Text(text = "تلاش مجدد")
            }
        }
    }
}


@Composable
fun ListOfAddressBox(addressList: List<AddressItem>) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(addressList, key = { it.id }) {
                AddressCard(
                    address = it.address,
                    fullName = it.getFullName(),
                    mobile = it.coordinate_mobile
                )
            }
        }
    }
}

@Composable
fun AddressCard(
    address: String,
    fullName: String,
    mobile: String
) {
    val context = LocalContext.current
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:$mobile"))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = address,
                fontSize = TextUnit(18F, TextUnitType.Sp),
                textAlign = TextAlign.Start
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp),
                color = Color.White
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = fullName)
                ElevatedButton(
                    onClick = { context.startActivity(intent) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = colorGreen
                    ),
                    contentPadding = PaddingValues(),
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .width(140.dp)
                        .defaultMinSize(
                            minWidth = 0.dp,
                            minHeight = 0.dp
                        )
                ) {
                    Text(text = mobile, fontSize = TextUnit(14F, TextUnitType.Sp))
                    Divider(
                        modifier = Modifier.width(8.dp),
                        color = Color.White.copy(alpha = 0F)
                    )
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}
