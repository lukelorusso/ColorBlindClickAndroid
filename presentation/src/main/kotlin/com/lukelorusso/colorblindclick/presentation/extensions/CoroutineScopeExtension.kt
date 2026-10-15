package com.lukelorusso.colorblindclick.presentation.extensions

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Launches [block] in this [CoroutineScope] (e.g. `viewModelScope`), rethrowing
 * [CancellationException] so structured-concurrency cancellation is never swallowed,
 * while sending all other [Throwable] via [onError] to be handled by the UI
 */
fun CoroutineScope.launchCatching(
    onError: (Throwable) -> Unit,
    block: suspend CoroutineScope.() -> Unit
): Job = launch {
    try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (t: Throwable) {
        onError(t)
    }
}
