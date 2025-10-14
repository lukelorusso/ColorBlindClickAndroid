package com.lukelorusso.presentation.ui.base

import com.lukelorusso.data.di.dataTestModule
import com.lukelorusso.domain.di.domainModule
import com.lukelorusso.presentation.di.presentationTestModule
import org.junit.Before
import org.junit.Rule
import org.koin.core.context.GlobalContext.startKoin
import org.koin.test.KoinTest

/**
 * Copyright (C) 2024 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
abstract class AppTest : KoinTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setupAppTest() {
        startKoin {
            modules(modules)
        }
    }

    companion object {
        private val modules = dataTestModule +
                domainModule +
                presentationTestModule
    }

}
