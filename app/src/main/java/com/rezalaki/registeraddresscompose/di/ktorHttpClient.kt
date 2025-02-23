package com.rezalaki.registeraddresscompose.di

import android.util.Log
import com.rezalaki.registeraddresscompose.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.DefaultRequest
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.client.request.host
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import java.util.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


@OptIn(ExperimentalEncodingApi::class)
val ktorHttpClient = HttpClient(Android) {
    defaultRequest {
        host = Constants.BASE_URL
        url {
            protocol = URLProtocol.HTTPS
        }
    }
    install(JsonFeature) {
        serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    engine {
        connectTimeout = Constants.TIME_OUT
        socketTimeout = Constants.TIME_OUT
    }
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Log.d("SERVER LOG", message)
            }
        }
        level = LogLevel.ALL
    }
    install(ResponseObserver) {
        onResponse { response ->
            Log.d("SERVER RSP", "${response.status.value}")
        }
    }
    install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        val username = "09822222222"
        val password = "Sana12345678"
        val credentials = "$username:$password"
        val encodedCredentials = kotlin.io.encoding.Base64.encode(credentials.toByteArray())
        header("Authorization", "Basic $encodedCredentials")
    }
}