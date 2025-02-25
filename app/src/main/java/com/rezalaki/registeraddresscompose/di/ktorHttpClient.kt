package com.rezalaki.registeraddresscompose.di


import android.util.Log
import com.rezalaki.registeraddresscompose.utils.Constants
import com.rezalaki.registeraddresscompose.utils.extensions.unescapeUnicode
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.gson.gson
import kotlin.io.encoding.ExperimentalEncodingApi


@OptIn(ExperimentalEncodingApi::class)
val ktorHttpClient = HttpClient(Android) {
    defaultRequest {
        host = Constants.BASE_URL
        url {
            protocol = URLProtocol.HTTPS
        }
        headers {
            header("Accept", "application/json")
            header("content-type", "application/json")
        }
    }
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            setLenient()
            serializeNulls()
        }
    }
    engine {
        connectTimeout = Constants.TIME_OUT
        socketTimeout = Constants.TIME_OUT
    }
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Log.d(
                    "SERVER",
                    message.unescapeUnicode()
                )
            }
        }
        level = LogLevel.ALL
    }
    install(ResponseObserver) {
        onResponse { response ->
            Log.d("SERVER RSP", "${response.status.value}")
        }
    }

    install(DefaultRequest){
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        val username = "09822222222"
        val password = "Sana12345678"
        val credentials = "$username:$password"
        val encodedCredentials = kotlin.io.encoding.Base64.encode(credentials.toByteArray())
        header("Authorization", "Basic $encodedCredentials")
    }

}