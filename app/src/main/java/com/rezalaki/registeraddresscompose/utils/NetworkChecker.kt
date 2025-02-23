package com.rezalaki.registeraddresscompose.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import java.util.Objects

class NetworkChecker(
    private val context: Context
) : LiveData<Boolean>() {

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = Objects.requireNonNull(connectivityManager).activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

}