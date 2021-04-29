package com.lukelorusso.presentation.scenes.camera

import com.lukelorusso.domain.repository.ColorRepository
import com.lukelorusso.domain.usecases.GetColor
import com.lukelorusso.presentation.scenes.base.viewmodel.AViewModel
import io.reactivex.rxjava3.core.Observable

/**
 * @author LukeLorusso on 26-03-2021.
 */
internal class CameraTestViewModel(
    repository: ColorRepository
) : AViewModel<CameraData>() {

    private val getColor = GetColor(repository)

    fun intentGetColor(param: GetColor.Param): Observable<CameraData> =
        getColor.execute(param)
            .toObservable()
            .map { CameraData.createColor(it) }
            .doOnError { println(getErrorMessage(it)) }

}
