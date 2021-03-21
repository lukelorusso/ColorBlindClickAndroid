package com.lukelorusso.presentation.exception

import android.content.Context
import com.lukelorusso.domain.exception.NoConnectedException
import com.lukelorusso.domain.exception.PersistenceException
import com.lukelorusso.domain.exception.WebServiceException
import com.lukelorusso.domain.usecases.base.Logger
import com.lukelorusso.presentation.R

/**
 * Copyright (C) 2020 Mikhael LOPEZ
 * Licensed under the Apache License Version 2.0
 * Factory used to create error messages from an Exception as a condition.
 */
interface ErrorMessageFactory {

    /**
     * Creates a String representing an error message.
     * @param exception An exception used as a condition to retrieve the correct error message.
     * @return [String] an error message.
     */
    fun getError(exception: Throwable?): String

    class Impl(private val context: Context, private val logger: Logger) : ErrorMessageFactory {
        override fun getError(exception: Throwable?): String = exception?.let {
            when (it) {
                is NoConnectedException -> context.getString(R.string.error_no_connection)
                is WebServiceException -> context.getString(R.string.error_web_service)
                is PersistenceException -> context.getString(R.string.error_persistence)
                else -> exception.localizedMessage ?: context.getString(R.string.error_generic)
            }.apply { logger.logError { it } }
        } ?: getGenericError()

        private fun getGenericError() = context.getString(R.string.error_generic)
    }

}
