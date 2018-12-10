package com.android.example.github.data.util

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Injectable class which returns information about the network connection state.
 */

@Suppress("unused")
@Singleton
class NetworkHandler @Inject constructor(private val context: Application) {
    val isConnected get() = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            .activeNetworkInfo?.isConnectedOrConnecting
}