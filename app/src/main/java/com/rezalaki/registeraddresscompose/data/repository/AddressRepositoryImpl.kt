package com.rezalaki.registeraddresscompose.data.repository

import android.util.Log
import com.rezalaki.registeraddresscompose.data.ApiHandler
import com.rezalaki.registeraddresscompose.data.model.addressList.AddressItem
import com.rezalaki.registeraddresscompose.data.model.submitAddress.SubmitAddressRequestBody
import com.rezalaki.registeraddresscompose.data.model.submitAddress.SubmitAddressResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AddressRepositoryImpl(
    private val httpClient: HttpClient
) : AddressRepository {

    override suspend fun submitForm(requestBody: SubmitAddressRequestBody): Flow<ApiHandler<SubmitAddressResponse>> =
        flow<ApiHandler<SubmitAddressResponse>> {
            emit(ApiHandler.loading())

            try {
                val response = httpClient.post("karfarmas/address") {
                    setBody(requestBody)
                }
                emit(
                    ApiHandler.success(response.body())
                )
            } catch (ex: Exception) {
                Log.d("TAGGGG", "error : ${ex.message}")
                emit(
                    ApiHandler.error(ex.message.orEmpty())
                )
            }


        }.catch {
            Log.d("TAGGGG", "flow error : ${it.message}")
            emit(ApiHandler.error(it.message.orEmpty()))
        }.flowOn(Dispatchers.IO)


    override suspend fun getAddressList(): Flow<ApiHandler<List<AddressItem>>> =
        flow<ApiHandler<List<AddressItem>>> {
            emit(ApiHandler.loading())

            try {
                val addressList = httpClient.get("karfarmas/address")
                emit(
                    ApiHandler.success(addressList.body())
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