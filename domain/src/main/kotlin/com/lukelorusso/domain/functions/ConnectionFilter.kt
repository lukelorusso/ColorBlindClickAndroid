package com.lukelorusso.domain.functions

import com.lukelorusso.domain.exception.NoConnectedException
import io.reactivex.rxjava3.functions.Predicate

/**
 * Copyright (C) 2020 Mikhael LOPEZ
 * Licensed under the Apache License Version 2.0
 * [Predicate] that returns true if there's an available internet connection
 * or throw [NoConnectedException] if not.
 */
class ConnectionFilter : Predicate<Boolean> {

    @Throws(Exception::class)
    override fun test(isConnected: Boolean): Boolean {
        if (!isConnected) {
            throw NoConnectedException
        }
        return true
    }

}
