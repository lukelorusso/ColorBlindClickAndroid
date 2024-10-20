package com.lukelorusso.data.datasource

import com.lukelorusso.domain.exception.NotConnectedException
import com.lukelorusso.domain.exception.WebServiceException
import retrofit2.Response

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
class HttpManager(
    private val networkChecker: NetworkChecker? = null
) {
    suspend fun <DTO, Entity> restCall(
        call: suspend () -> Response<DTO>,
        mapper: (DTO) -> Entity
    ): Entity {
        if (networkChecker?.isConnected == false)
            throw NotConnectedException()
        else {
            val response = call()
            if (response.isSuccessful)
                return mapper(response.body() ?: throw WebServiceException())
            else
                throw WebServiceException()
        }
    }
}
