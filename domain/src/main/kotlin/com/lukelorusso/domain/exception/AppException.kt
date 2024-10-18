package com.lukelorusso.domain.exception

sealed class AppException(message: String) : RuntimeException(message)

/**
 * Exception used when it is impossible to get data due to a lack of connection
 */
class NotConnectedException : AppException("No connection")

/**
 * Exception used when the web service does not respond as expected
 */
class WebServiceException : AppException("The web service did not respond as expected")
