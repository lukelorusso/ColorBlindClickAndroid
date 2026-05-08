package com.lukelorusso.colorblindclick.domain.usecase.base

interface Logger {
    fun log(message: () -> String)
    fun logError(throwable: () -> Throwable)
}
