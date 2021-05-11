package com.lukelorusso.presentation.usecase.camera

import com.lukelorusso.presentation.base.ABaseTest
import io.reactivex.rxjava3.core.Single
import org.junit.Test

class CameraTest : ABaseTest() {

    @Test
    fun testGetColor() {
        Single.just("#6B8E23")
            .test()
            .assertValue { it == "#6B8E23" }
    }

}
