package com.lukelorusso.data.net

import android.content.Context
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
open class OkHttpClientFactory {

    open fun createOkHttpClient(context: Context? = null): OkHttpClient =
        OkHttpClient.Builder()
            .apply { updateTimeout() }
            .build()

    private fun OkHttpClient.Builder.updateTimeout(read: Long = 60, write: Long = 60) {
        readTimeout(read, TimeUnit.SECONDS)
        writeTimeout(write, TimeUnit.SECONDS)
    }

}
