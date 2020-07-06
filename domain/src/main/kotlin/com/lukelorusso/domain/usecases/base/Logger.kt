package com.lukelorusso.domain.usecases.base

interface Logger {
    fun log(message: () -> String)
    fun logError(throwable: () -> Throwable)
}
