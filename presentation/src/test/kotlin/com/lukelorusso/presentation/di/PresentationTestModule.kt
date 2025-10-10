package com.lukelorusso.presentation.di

import com.lukelorusso.domain.usecase.base.Logger
import com.lukelorusso.presentation.helper.TrackerHelper
import com.lukelorusso.presentation.helper.impl.TrackerHelperImplTest
import org.koin.dsl.module

/**
 * Copyright (C) 2021 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
val presentationTestModule = module {
    //region AppComponent
    factory<Logger> {
        object : Logger {
            override fun log(message: () -> String) {
                println(message())
            }

            override fun logError(throwable: () -> Throwable) {
                println(throwable())
            }
        }
    }
    factory<TrackerHelper> { TrackerHelperImplTest() }
    //endregion
}
