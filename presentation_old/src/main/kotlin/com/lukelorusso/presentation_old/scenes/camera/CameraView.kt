package com.lukelorusso.presentation_old.scenes.camera

import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation_old.scenes.base.view.LoadDataView
import io.reactivex.rxjava3.core.Observable

interface CameraView : LoadDataView<CameraViewModel> {

    fun intentLoadData(): Observable<Unit>

    fun intentSetLastLensPosition(): Observable<Int>

    fun intentGotoInfo(): Observable<Unit>

    fun intentGotoHistory(): Observable<Unit>

    fun intentGetColor(): Observable<Pair<String, String>>

    fun intentOpenPreview(): Observable<Color>

}
