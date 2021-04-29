package com.lukelorusso.presentation.scenes.base.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.lukelorusso.data.helper.TimberWrapper
import com.lukelorusso.domain.usecases.base.Logger

/**
 * @author LukeLorusso on 25-03-2021.
 */
abstract class TestLifecycleOwner : LifecycleOwner {

    lateinit var logger: Logger
    var onSuccess: ((Any) -> Unit)? = null
    private lateinit var lifecycleRegistry: LifecycleRegistry

    fun startLifecycle() {
        lifecycleRegistry = LifecycleRegistry(this)
        logger = object : Logger {
            override fun log(message: () -> String) {
                TimberWrapper.d(message)
            }

            override fun logError(throwable: () -> Throwable) {
                TimberWrapper.e(throwable)
            }
        }
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
    }

    fun stopLifecycle() {
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        onSuccess = null
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

}
