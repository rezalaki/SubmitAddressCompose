package com.rezalaki.registeraddresscompose.di

import com.rezalaki.registeraddresscompose.data.model.submitAddress.SubmitAddressRequestBody
import com.rezalaki.registeraddresscompose.data.repository.AddressRepository
import com.rezalaki.registeraddresscompose.data.repository.AddressRepositoryImpl
import com.rezalaki.registeraddresscompose.ui.screen.addressListScreen.AddressViewModel
import com.rezalaki.registeraddresscompose.ui.screen.formScreen.FormViewModel
import com.rezalaki.registeraddresscompose.ui.screen.mapScreen.MapViewModel
import com.rezalaki.registeraddresscompose.utils.NetworkChecker
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val appModules = module {
    single { ktorHttpClient }
    single<AddressRepository> { AddressRepositoryImpl(get()) }

    factory { NetworkChecker(get()) }

    viewModel { FormViewModel() }
    viewModel { (formRequestBody: SubmitAddressRequestBody) ->
        MapViewModel(
            formRequestBody,
            get(),
            get()
        )
    }
    viewModel { AddressViewModel(get(), get()) }
}

