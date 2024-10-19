package com.lukelorusso.presentation.ui.base

import com.lukelorusso.data.di.dataTestModule
import com.lukelorusso.domain.di.domainModule
import com.lukelorusso.presentation.di.presentationTestModule
import org.junit.Rule
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule

/**
 * Copyright (C) 2024 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
abstract class AppTest : KoinTest {

    companion object {
        private val modules = dataTestModule +
                domainModule +
                presentationTestModule
    }

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(modules)
    }

}
