package com.lukelorusso.presentation.analytics

import com.lukelorusso.presentation.BuildConfig
import org.junit.Test

class AnalyticsTest {

    @Test
    fun analyticsShouldBeOffForDebugTests() {
        val enabled = !BuildConfig.DEBUG
        assert(BuildConfig.ENABLE_ANALYTICS == enabled)
    }
}
