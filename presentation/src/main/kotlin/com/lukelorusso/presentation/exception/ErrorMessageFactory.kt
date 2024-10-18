package com.lukelorusso.presentation.exception

import android.content.Context
import com.lukelorusso.domain.exception.NotConnectedException
import com.lukelorusso.domain.exception.WebServiceException
import com.lukelorusso.domain.usecase.base.Logger
import com.lukelorusso.presentation.R

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
interface ErrorMessageFactory {

    /**
     * Creates a String representing an error message.
     * @param exception An exception used as a condition to retrieve the correct error message.
     * @return [String] an error message.
     */
    fun getError(exception: Throwable): String

    class Impl(private val context: Context, private val logger: Logger) : ErrorMessageFactory {
        override fun getError(exception: Throwable): String = when (exception) {
            is NotConnectedException -> context.getString(R.string.error_no_connection)
            is WebServiceException -> context.getString(R.string.error_web_service)
            //is PersistenceException -> context.getString(R.string.error_persistence)
            else -> exception.message ?: context.getString(R.string.error_generic)
        }.apply { logger.logError { exception } }
    }

    class TestImpl : ErrorMessageFactory {
        override fun getError(exception: Throwable): String =
            "${exception.javaClass}: ${exception.message}"
    }

}
