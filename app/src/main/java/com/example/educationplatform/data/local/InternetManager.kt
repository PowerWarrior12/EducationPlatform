package com.example.educationplatform.data.local

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import javax.inject.Inject


class InternetManager @Inject constructor(private val context: Context) {

    fun checkInternetConnection(): Boolean {
        Build.VERSION_CODES.M
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return connectivityManager?.let {
            val activeNetwork = connectivityManager.activeNetwork
            var result: Boolean
            connectivityManager.getNetworkCapabilities(activeNetwork).also { networkCapabilities ->
                result = networkCapabilities!= null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }
            result

        } ?: false
    }
}