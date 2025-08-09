package com.lukelorusso.domain.usecase.base

import org.koin.java.KoinJavaComponent.inject

/**
 * Copyright (C) 2024 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 *
 * Abstract class for a Use Case
 * This interface represents an execution unit for different use cases (this means any use case
 * in the application should implement this contract)
 */
abstract class UseCase<in Params, out Result>(
    open val logResult: Boolean = true
) {
    private val logger: Logger by inject(Logger::class.java)
    protected abstract suspend fun run(param: Params): Result

    /**
     * Executes the current use case.
     */
    @Throws
    suspend operator fun invoke(params: Params): Result {
        return try {
            logger.log { "$USE_CASE_PREFIX ${javaClass.simpleName} starts with params: $params" }
            run(params).also { result ->
                if (logResult)
                    logger.log { "$USE_CASE_PREFIX ${javaClass.simpleName} emits $result" }
                else
                    logger.log { "$USE_CASE_PREFIX ${javaClass.simpleName} emits a result (hidden)" }
                logger.log { "$USE_CASE_PREFIX ${javaClass.simpleName} completes" }
            }
        } catch (e: Exception) {
            logger.logError { e }
            throw e
        }
    }

    companion object {
        private const val USE_CASE_PREFIX = "UseCase"
    }
}
