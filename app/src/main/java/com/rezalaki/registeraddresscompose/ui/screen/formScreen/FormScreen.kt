package com.rezalaki.registeraddresscompose.ui.screen.formScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rezalaki.registeraddresscompose.R
import com.rezalaki.registeraddresscompose.data.model.submitAddress.SubmitAddressRequestBody
import com.rezalaki.registeraddresscompose.ui.components.HeaderTop
import com.rezalaki.registeraddresscompose.ui.theme.colorBackground
import com.rezalaki.registeraddresscompose.ui.theme.colorBlue
import com.rezalaki.registeraddresscompose.ui.theme.colorGray
import com.rezalaki.registeraddresscompose.ui.theme.colorGreen
import com.rezalaki.registeraddresscompose.ui.theme.colorRed
import com.rezalaki.registeraddresscompose.utils.ValidationRegexes
import org.koin.androidx.compose.koinViewModel


private enum class IconState(val colorValue: Color, val iconResource: Int) {
    UNSELECT(
        colorGray,
        R.drawable.ic_circle_filled
    ),
    VALID(
        colorGreen,
        R.drawable.ic_check_circle
    ),
    INVALID(
        colorRed,
        R.drawable.ic_close
    )
}


@Composable
fun FormScreen(
    goToMap: (fromBody: SubmitAddressRequestBody) -> Unit,
    showError: (errorMessageList: List<String>) -> Unit,
    viewModel: FormViewModel = koinViewModel()
) {
    val ui = viewModel.uiState.collectAsStateWithLifecycle(initialValue = FormUiState.Idle)

    LaunchedEffect(key1 = ui.value) {
        when (ui.value) {
            is FormUiState.GoNextScreen -> {
                goToMap.invoke(viewModel.formBody)
            }

            FormUiState.Idle -> {}

            is FormUiState.ShowErrors -> {
                val errorList = (ui.value as FormUiState.ShowErrors).errors
                showError.invoke(errorList)
            }
        }
    }

    FormScreenContent(viewModel)
}

@Composable
private fun FormScreenContent(
    viewModel: FormViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorBackground)
            .verticalScroll(rememberScrollState())
    ) {
        HeaderTop(
            title = "ثبت نام",
            onBackClicked = {}
        )
        Text(
            text = "لطفا اطلاعات خود را وارد کنید",
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .background(colorBackground)
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .align(Alignment.Start),
            textAlign = TextAlign.Start
        )
        HorizontalDivider(thickness = 32.dp, color = Color.White)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(bottom = 20.dp)
        ) {
            FormFiled(
                viewModel.formBody.first_name.orEmpty(),
                false,
                "نام",
                false,
                ValidationRegexes.NAME
            ) {
                viewModel.formBody.first_name = it
            }
            FormFiled(
                viewModel.formBody.last_name.orEmpty(),
                false,
                "نام خانوادگی",
                false,
                ValidationRegexes.LAST_NAME
            ) {
                viewModel.formBody.last_name = it
            }
            FormFiled(
                viewModel.formBody.coordinate_mobile.orEmpty(),
                false,
                "تلفن همراه",
                true,
                ValidationRegexes.MOBILE
            ) {
                viewModel.formBody.coordinate_mobile = it
            }
            FormFiled(
                viewModel.formBody.coordinate_phone_number.orEmpty(),
                false,
                "تلفن ثابت",
                true,
                ValidationRegexes.PHONE
            ) {
                viewModel.formBody.coordinate_phone_number = it
            }
        }
        HorizontalDivider(thickness = 32.dp, color = colorBackground)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 20.dp)
        ) {
            FormFiled(
                viewModel.formBody.address.orEmpty(),
                labelTop = true,
                label = "آدرس دقیق",
                regexValidation = ValidationRegexes.ADDRESS
            ) {
                viewModel.formBody.address = it
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(0.dp)
                    .padding(start = 16.dp, end = 16.dp, top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "جنیست",
                    modifier = Modifier.fillMaxHeight(),
                    textAlign = TextAlign.Center,
                    lineHeight = TextUnit(40F, TextUnitType.Sp),
                    color = colorBlue
                )
                FormToggleGender(
                    defaultValueIsMan = viewModel.formBody.gender.orEmpty() == "MAIL"
                ) {
                    viewModel.formBody.gender = if (it) "MAIL" else "FEMAIL"
                }
            }
        }
        HorizontalDivider(thickness = 32.dp, color = colorBackground)
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorGreen
            ),
            shape = RoundedCornerShape(6.dp),
            onClick = {
                viewModel.submit()
            }
        ) {
            Text(text = "مرحله بعد", fontSize = 18.sp)
        }
        HorizontalDivider(thickness = 16.dp, color = colorBackground)
    }
}


@Composable
private fun FormFiled(
    defaultValue: String,
    labelTop: Boolean = false,
    label: String,
    onlyNumber: Boolean = false,
    regexValidation: String,
    inputValueChange: (String) -> Unit,
) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    val borderColor by remember {
        derivedStateOf {
            if (isFocused) colorBlue
            else colorGray
        }
    }

    val inputValue = remember {
        mutableStateOf(defaultValue)
    }
    val iconState = remember {
        mutableStateOf(IconState.UNSELECT)
    }


    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (labelTop) {
            Text(
                text = label,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Start,
                color = colorBlue
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .border(1.dp, color = borderColor, shape = RoundedCornerShape(4.dp))
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (labelTop.not()) {
                Text(
                    text = label,
                    color = colorBlue,
                    modifier = Modifier.fillMaxWidth(0.25F)
                )
            }
            BasicTextField(
                value = inputValue.value,
                onValueChange = {
                    if (label.contains("تلفن") && it.length > 11) {
                        return@BasicTextField
                    }

                    iconState.value = if (it.isEmpty()) {
                        IconState.UNSELECT
                    } else {
                        if (Regex(regexValidation).matches(it)) IconState.VALID
                        else IconState.INVALID
                    }
                    inputValue.value = it
                    inputValueChange.invoke(it)
                },
                modifier = Modifier
                    .fillMaxWidth(if (labelTop) 0.9F else 0.8F)
                    .fillMaxHeight()
                    .wrapContentHeight(Alignment.CenterVertically)
                    .padding(horizontal = 2.dp)
                    .onFocusChanged {
                        isFocused = it.isFocused
                    },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    textDirection = TextDirection.Rtl
                ),
                singleLine = true,
                keyboardOptions =
                KeyboardOptions(keyboardType = if (onlyNumber) KeyboardType.Number else KeyboardType.Text)
            )
            Icon(
                painterResource(id = iconState.value.iconResource),
                contentDescription = "",
                tint = iconState.value.colorValue
            )

        }
    }
}

@Composable
private fun FormToggleGender(
    defaultValueIsMan: Boolean,
    onValueChange: (isMan: Boolean) -> Unit
) {
    var isManSelected by remember {
        mutableStateOf(defaultValueIsMan)
    }
    Row(
        modifier = Modifier
            .width(180.dp)
            .height(40.dp)
            .border(width = 1.dp, color = colorBlue)
            .clip(shape = RoundedCornerShape(4.dp))
    ) {
        Text(
            text = "خانم",
            modifier = Modifier
                .weight(1F)
                .fillMaxHeight()
                .padding(0.dp)
                .then(
                    if (isManSelected)
                        Modifier
                    else
                        Modifier.background(colorBlue)
                )
                .clickable {
                    isManSelected = false
                    onValueChange.invoke(false)
                },
            textAlign = TextAlign.Center,
            lineHeight = TextUnit(40F, TextUnitType.Sp),
            color = if (isManSelected.not()) Color.White else colorBlue
        )
        Text(
            text = "آقا",
            modifier = Modifier
                .weight(1F)
                .height(40.dp)
                .padding(0.dp)
                .then(
                    if (isManSelected)
                        Modifier.background(colorBlue)
                    else
                        Modifier
                )
                .padding(top = 0.dp)
                .clickable {
                    isManSelected = true
                    onValueChange.invoke(true)
                },

            textAlign = TextAlign.Center,
            lineHeight = TextUnit(40F, TextUnitType.Sp),
            color = if (isManSelected) Color.White else colorBlue
        )
    }
}


//@Preview(showBackground = true)
@Composable
fun FormScreenPreview() {
//    FormScreen(
//        goToMap = {}
//    )
}

@Preview(showBackground = true)
@Composable
fun FormFiledPreview() {
    FormFiled(
        defaultValue = "",
        labelTop = false,
        label = "نام خانوادگی",
        regexValidation = ""
    ) {

    }
}

