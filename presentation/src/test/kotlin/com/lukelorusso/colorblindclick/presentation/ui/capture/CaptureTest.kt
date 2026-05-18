package com.lukelorusso.colorblindclick.presentation.ui.capture

import com.lukelorusso.colorblindclick.presentation.ui.base.AppTest
import com.lukelorusso.colorblindclick.presentation.ui.base.ContentState
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.koin.core.component.get

class CaptureTest : AppTest() {

    private lateinit var viewModel: CaptureViewModel

    @Before
    fun setup() {
        viewModel = CaptureViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }

    @Test
    fun decodeColorHex(): Unit = runBlocking {
        viewModel.decodeColor("#52851E")
        val result = viewModel.uiState.take(2)
        assert(
            result.filter { it.contentState == ContentState.CONTENT }.firstOrNull()?.color?.let { color ->
                color.colorHex == "#549019"
                        && color.colorName == "Vida Loca"
                        && color.originalColorHex() == "#52851E"
            } == true
        )
    }
}
