package com.rezalaki.registeraddresscompose.data

data class ApiHandler<out T>(
    val state: ApiHandlerState,
    val data: Any? = null,
    val errorMessage:String? = null
) {
    companion object {
        fun <T> loading(): ApiHandler<T> = ApiHandler(ApiHandlerState.LOADING)
        fun <T> error(errorMessage: String): ApiHandler<T> = ApiHandler(ApiHandlerState.FAILED, errorMessage = errorMessage)
        fun <T> success(result: T): ApiHandler<T> = ApiHandler(ApiHandlerState.SUCCESS, data = result)
    }
}

enum class ApiHandlerState {
    SUCCESS, FAILED, LOADING
}