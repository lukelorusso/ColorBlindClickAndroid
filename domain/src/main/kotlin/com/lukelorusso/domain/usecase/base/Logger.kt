package com.lukelorusso.domain.usecase.base

interface Logger {
    fun log(message: () -> String)
    fun logError(throwable: () -> Throwable)
}
