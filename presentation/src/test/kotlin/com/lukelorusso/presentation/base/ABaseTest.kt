package com.lukelorusso.presentation.base

import com.lukelorusso.data.di.dataTestModule
import com.lukelorusso.domain.di.domainModule
import com.lukelorusso.presentation.di.testModule
import org.junit.Rule
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule

abstract class ABaseTest : KoinTest {

    companion object {
        private val modules = dataTestModule +
                domainModule +
                testModule
    }

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(modules)
    }

}
