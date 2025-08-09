package com.lukelorusso.presentation.ui.capture

import androidx.lifecycle.ViewModel
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.usecase.DecodeColorHexUseCase
import com.lukelorusso.presentation.ui.base.AppTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.get

class CaptureTest : AppTest() {

    private lateinit var viewModel: CaptureTestViewModel

    @KoinApiExtension
    @Before
    fun setup() {
        viewModel = CaptureTestViewModel(get())
    }

    @Test
    fun decodeColorHex(): Unit = runBlocking {
        val colorModel = viewModel.decodeColorHex(DecodeColorHexUseCase.Param(hex = "#52851E", deviceUdid = "JUnit"))
        assert(colorModel.originalColorHex() == "#52851E")
    }

    class CaptureTestViewModel(private val decodeColorHex: DecodeColorHexUseCase) : ViewModel() {
        suspend fun decodeColorHex(param: DecodeColorHexUseCase.Param): Color =
            decodeColorHex.invoke(param)
    }
}
