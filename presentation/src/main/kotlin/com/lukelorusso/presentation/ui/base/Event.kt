package com.lukelorusso.presentation.ui.base

/**
 * Used as a wrapper for events exposed via LiveData
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again
     */
    fun contentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled
     */
    fun content(): T = content

}
