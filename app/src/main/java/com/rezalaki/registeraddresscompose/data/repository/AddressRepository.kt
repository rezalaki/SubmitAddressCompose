package com.rezalaki.registeraddresscompose.data.repository

import com.rezalaki.registeraddresscompose.data.ApiHandler
import com.rezalaki.registeraddresscompose.data.model.addressList.AddressItem
import com.rezalaki.registeraddresscompose.data.model.submitAddress.SubmitAddressRequestBody
import com.rezalaki.registeraddresscompose.data.model.submitAddress.SubmitAddressResponse
import kotlinx.coroutines.flow.Flow

interface AddressRepository {

    suspend fun submitForm(body: SubmitAddressRequestBody): Flow<ApiHandler<SubmitAddressResponse>>

    suspend fun getAddressList(): Flow<ApiHandler<List<AddressItem>>>

}