package com.lukelorusso.data.extensions

import com.lukelorusso.domain.exception.AppException
import com.lukelorusso.domain.exception.PersistenceException
import com.lukelorusso.domain.exception.WebServiceException
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleSource

@Throws(Exception::class)
fun <T> Throwable.catchWebServiceException(): SingleSource<T> = Single.just(this).flatMap {
    if (it is NullPointerException) {
        Single.error<T>(WebServiceException)
    } else {
        Single.error<T>(it)
    }
}

@Throws(Exception::class)
fun <T> Throwable.catchPersistenceException(): SingleSource<T> = Single.just(this).flatMap {
    if (it is RuntimeException && it !is AppException) {
        Single.error<T>(PersistenceException(it.message))
    } else {
        Single.error<T>(it)
    }
}
