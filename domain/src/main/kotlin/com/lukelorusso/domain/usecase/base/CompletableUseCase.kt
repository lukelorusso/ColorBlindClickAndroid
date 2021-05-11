package com.lukelorusso.domain.usecase.base

import io.reactivex.rxjava3.core.Completable

/**
 * Copyright (C) 2020 Mikhael LOPEZ
 * Licensed under the Apache License Version 2.0
 */
abstract class CompletableUseCase<in P> : UseCase<Completable, P>() {

    override fun execute(param: P, fromUseCase: Boolean): Completable =
        super.execute(param, fromUseCase)
            .compose { transformer ->
                useCaseScheduler.let {
                    if (fromUseCase) transformer
                    else transformer.subscribeOn(it.run).observeOn(it.post)
                } ?: transformer
            }
            .doOnError { logger.logError { it } }
            .doOnComplete { logger.log { "${javaClass.simpleName} : $param => completed" } }

}
