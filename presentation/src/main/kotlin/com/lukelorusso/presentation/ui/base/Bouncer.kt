package com.lukelorusso.presentation.ui.base

import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger

class Bouncer(
    private val bounceDelayInMillis: Long = 150L,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private val lastInput = AtomicInteger(0)

    private var job: Job? = null

    /**
     * Execute a job after a delay, if no one else is re-bounced in the meantime
     */
    fun bounce(callback: suspend () -> Unit) {
        val currentInput = tick()

        job = CoroutineScope(dispatcher).launch {
            delay(bounceDelayInMillis)

            if (currentInput == lastInput.get()) {
                callback()
            }
        }
    }

    /**
     * Cancel any previous job, if not started yet
     */
    fun tick(): Int {
        job?.cancel()
        return lastInput.incrementAndGet()
    }
}
