package com.lukelorusso.presentation.ui.camera

import androidx.lifecycle.ViewModel
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.usecase.GetColorUseCase
import com.lukelorusso.presentation.ui.base.ABaseTest
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Test
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.get

class CameraTest : ABaseTest() {

    private lateinit var viewModel: CameraTestViewModel

    @KoinApiExtension
    @Before
    fun setup() {
        viewModel = CameraTestViewModel(get())
    }

    @Test
    fun intentGetColor() {
        viewModel.intentGetColor(GetColorUseCase.Param(hex = "#52851E", deviceUdid = "JUnit"))
            .test()
            .await()
            .assertNoErrors()
            .assertComplete()
            //.assertValue { it.colorHex == "#6B8E23" } // this might change in the remote DB...
            .assertValue { it.originalColorHex() == "#52851E" } // this won't change for newer versions
    }

    class CameraTestViewModel(private val getColor: GetColorUseCase) : ViewModel() {

        internal fun intentGetColor(param: GetColorUseCase.Param): Observable<Color> =
            getColor.execute(param).toObservable()

    }

}
