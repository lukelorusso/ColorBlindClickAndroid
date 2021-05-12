package com.lukelorusso.presentation.ui.camera

import androidx.lifecycle.ViewModel
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.usecase.GetColorUseCase
import com.lukelorusso.presentation.ui.base.ABaseTest
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Test
import org.koin.core.component.get

class CameraTest : ABaseTest() {

    private lateinit var viewModel: CameraTestViewModel

    @Before
    fun setup() {
        viewModel = CameraTestViewModel(get())
    }

    @Test
    fun intentGetColor() {
        viewModel.intentGetColor(GetColorUseCase.Param(colorHex = "#52851E", deviceUdid = "JUnit"))
            .test()
            .await()
            .assertNoErrors()
            .assertComplete()
            .assertValue { it.similarColor == "#6B8E23" }
    }

    class CameraTestViewModel(private val getColor: GetColorUseCase) : ViewModel() {

        internal fun intentGetColor(param: GetColorUseCase.Param): Observable<Color> =
            getColor.execute(param).toObservable()

    }

}
