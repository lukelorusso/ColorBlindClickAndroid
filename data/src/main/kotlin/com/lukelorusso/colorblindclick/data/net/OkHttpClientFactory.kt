package com.lukelorusso.colorblindclick.data.net

import android.content.Context
import com.lukelorusso.colorblindclick.data.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Copyright (C) 2024 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
open class OkHttpClientFactory {

    open fun createOkHttpClient(context: Context? = null): OkHttpClient =
        OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    enableDebugTools(context)
                }
                updateTimeout()
            }
            .build()


    private fun OkHttpClient.Builder.enableDebugTools(context: Context?) {
        context?.also {
            addInterceptor(
                HttpLoggingInterceptor(REDACTING_LOGGER).apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            )
        }
    }

    private fun OkHttpClient.Builder.updateTimeout(read: Long = 60, write: Long = 60) {
        readTimeout(read, TimeUnit.SECONDS)
        writeTimeout(write, TimeUnit.SECONDS)
    }

    companion object {
        /**
         * Query params whose values must never reach logcat/bug-reports, even in debug builds
         * (e.g. "udid" is a device identifier sent to the SaveDev API).
         */
        private val SENSITIVE_QUERY_PARAMS = listOf("udid")

        private val REDACTING_LOGGER = HttpLoggingInterceptor.Logger { message ->
            Timber.tag("OkHttp").d(redactSensitiveQueryParams(message))
        }

        /**
         * For each sensitive param, replace only its value (text after "?param=" or "&param=",
         * up to the next "&" or end of line) with "REDACTED", leaving the rest of the log line intact.
         */
        private fun redactSensitiveQueryParams(message: String): String =
            SENSITIVE_QUERY_PARAMS.fold(message) { acc, param ->
                acc.replace(Regex("(?<=[?&]$param=)[^&\\s]+"), "REDACTED")
            }
    }

}
