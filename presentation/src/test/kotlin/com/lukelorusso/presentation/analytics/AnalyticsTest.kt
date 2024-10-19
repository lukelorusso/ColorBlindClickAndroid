package com.lukelorusso.presentation.analytics

import com.lukelorusso.presentation.BuildConfig
import org.junit.Test

class AnalyticsTest {

    @Test
    fun analyticsShouldBeOffDuringTests() {
        assert(!BuildConfig.ENABLE_ANALYTICS)
    }
}
