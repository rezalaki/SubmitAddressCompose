package com.rezalaki.registeraddresscompose.data.repository

import com.rezalaki.registeraddresscompose.data.ApiHandler
import com.rezalaki.registeraddresscompose.data.model.addressList.AddressItem
import com.rezalaki.registeraddresscompose.data.model.submitAddress.SubmitAddressRequestBody
import com.rezalaki.registeraddresscompose.data.model.submitAddress.SubmitAddressResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AddressRepositoryImpl(
    private val httpClient: HttpClient
) : AddressRepository {

    override suspend fun submitForm(requestBody: SubmitAddressRequestBody): Flow<ApiHandler<SubmitAddressResponse>> =
        flow {
            emit(ApiHandler.loading())

            try {
                val response = httpClient.post<SubmitAddressResponse>("karfarmas/address") {
                    body = requestBody
                }
                emit(
                    ApiHandler.success(response)
                )
            } catch (ex: Exception) {
                emit(
                    ApiHandler.error(ex.message.orEmpty())
                )
            }


        }.catch {
            emit(ApiHandler.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)


    override suspend fun getAddressList(): Flow<ApiHandler<List<AddressItem>>> =
        flow {
            emit(ApiHandler.loading())

            try {
                val addressList = httpClient.get<List<AddressItem>>("karfarmas/address")
                emit(
                    ApiHandler.success(addressList)
                )

            } catch (ex: Exception) {
                emit(
                    ApiHandler.error(ex.message.orEmpty())
                )
            }

        }.catch {
            emit(ApiHandler.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)


}