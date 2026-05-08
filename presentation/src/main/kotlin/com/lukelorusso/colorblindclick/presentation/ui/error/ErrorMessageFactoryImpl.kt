package com.lukelorusso.colorblindclick.presentation.ui.error

import android.content.Context
import com.lukelorusso.colorblindclick.domain.exception.NotConnectedException
import com.lukelorusso.colorblindclick.domain.exception.WebServiceException
import com.lukelorusso.colorblindclick.domain.usecase.base.Logger
import com.lukelorusso.colorblindclick.presentation.R
import com.lukelorusso.colorblindclick.presentation.error.ErrorMessageFactory

class ErrorMessageFactoryImpl(
    private val context: Context,
    private val logger: Logger
) : ErrorMessageFactory {
    override fun getLocalizedMessage(exception: Throwable): String = when (exception) {
        is NotConnectedException -> context.getString(R.string.error_no_connection)
        is WebServiceException -> context.getString(R.string.error_web_service)
        else -> exception.message ?: context.getString(R.string.error_generic)
    }.apply { logger.logError { exception } }
}
