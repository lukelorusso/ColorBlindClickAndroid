package com.lukelorusso.domain.usecase.base

import org.koin.java.KoinJavaComponent.inject

/**
 * Abstract class for a Use Case
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 *
 * By convention each Use Case implementation will return the result using a [Subscriber]
 * that will execute its job in a background thread and will post the result in the UI thread.
 */
abstract class UseCase<R, in P> {

    internal val logger by inject(Logger::class.java)
    internal val useCaseScheduler by inject(UseCaseScheduler::class.java)

    /**
     * Builds which will be used when executing the current [UseCase].
     */
    protected abstract fun build(param: P): R

    /**
     * Executes the current use case.
     */
    fun execute(param: P): R = execute(param, false)

    /**
     * To not apply transformer with [UseCaseScheduler]
     * This method can be used just in domain module (internal).
     */
    internal fun executeFromAnOtherUseCase(param: P): R = execute(param, true)

    protected open fun execute(param: P, fromUseCase: Boolean): R {
        logger.log { "${javaClass.simpleName} : $param" }
        return build(param)
    }

}
