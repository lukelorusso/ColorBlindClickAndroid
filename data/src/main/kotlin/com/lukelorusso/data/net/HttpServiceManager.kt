package com.lukelorusso.data.net

import com.lukelorusso.data.di.providers.NetworkChecker
import com.lukelorusso.data.extensions.catchPersistenceException
import com.lukelorusso.data.extensions.catchWebServiceException
import com.lukelorusso.domain.exception.NotConnectedException
import io.reactivex.rxjava3.core.Single

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
class HttpServiceManager(
    private val networkChecker: NetworkChecker? = null
) {
    @Suppress("ThrowsCount")
    fun <DTO, Entity> execute(
        call: Single<DTO>,
        mapper: (DTO) -> Entity
    ): Single<Entity> =
        if (networkChecker?.isConnected == false)
            throw NotConnectedException
        else
            call.map(mapper)
                .onErrorResumeNext { e -> e.catchWebServiceException() }
                .onErrorResumeNext { e -> e.catchPersistenceException() }
}
