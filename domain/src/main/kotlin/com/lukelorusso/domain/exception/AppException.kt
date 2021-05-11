package com.lukelorusso.domain.exception

sealed class AppException(message: String) : RuntimeException(message)

/**
 * Exception used when it is impossible to get data due to a lack of connection
 */
object NotConnectedException : AppException("No connection")

/**
 * Exception used when the web service does not respond as expected
 */
object WebServiceException : AppException("The web service did not respond as expected")

/**
 * Exception used when persistence method return false on SingleUseCase
 */
class PersistenceException(message: String? = null) : AppException(
    "Data persistence error occurred" +
            if (message != null) ": $message" else ""
)
