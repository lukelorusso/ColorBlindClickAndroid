package com.lukelorusso.presentation.error

/**
 * Copyright (C) 2024 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
interface ErrorMessageFactory {

    /**
     * Creates a String representing an error message.
     * @param exception An exception used as a condition to retrieve the correct error message.
     * @return [String] an error message.
     */
    fun getLocalizedMessage(exception: Throwable): String

}
