package com.lukelorusso.colorblindclick.presentation.analytics

import com.lukelorusso.colorblindclick.presentation.BuildConfig
import org.junit.Test

class AnalyticsTest {

    @Test
    fun analyticsShouldBeOffForDebugTests() {
        val enabled = !BuildConfig.DEBUG
        assert(BuildConfig.ENABLE_ANALYTICS == enabled)
    }
}
