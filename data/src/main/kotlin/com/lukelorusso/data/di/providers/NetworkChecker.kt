package com.lukelorusso.data.di.providers

import android.content.Context
import com.lukelorusso.data.extensions.isInternetAvailable

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 *
 * Exposing methods to determine network's status.
 */
class NetworkChecker(private val context: Context) {

    val isConnected: Boolean
        get() = context.isInternetAvailable()

}
