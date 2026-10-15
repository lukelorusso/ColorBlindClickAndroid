package com.lukelorusso.colorblindclick.presentation.ui.base

import com.lukelorusso.colorblindclick.presentation.extensions.launchCatching
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration.Companion.milliseconds

class Bouncer(
    private val bounceDelayInMillis: Long = 150L,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private val lastInput = AtomicInteger(0)

    private var job: Job? = null

    /**
     * Execute a job after a delay, if no one else is re-bounced in the meantime.
     * Launched in [scope] (e.g. `viewModelScope`) so cancellation follows the
     * caller's lifecycle; any non-cancellation [Throwable] from [callback] is
     * routed to [onError] instead of being silently dropped.
     */
    fun bounce(
        scope: CoroutineScope,
        onError: (Throwable) -> Unit = {},
        callback: suspend () -> Unit
    ) {
        val currentInput = tick()

        job = scope.launchCatching(onError = onError) {
            withContext(dispatcher) {
                delay(bounceDelayInMillis.milliseconds)

                if (currentInput == lastInput.get()) {
                    callback()
                }
            }
        }
    }

    /**
     * Cancel any previous job, if not started yet.
     */
    fun tick(): Int {
        job?.cancel()
        return lastInput.incrementAndGet()
    }
}

