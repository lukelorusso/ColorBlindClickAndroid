package com.lukelorusso.presenter.scenes.camera

import com.lukelorusso.data.helper.TimberWrapper
import com.lukelorusso.domain.repository.ColorRepository
import com.lukelorusso.domain.usecases.GetColor
import com.lukelorusso.presentation.exception.ErrorMessageFactory
import com.lukelorusso.presentation.scenes.base.viewmodel.AViewModel
import com.lukelorusso.presentation.scenes.camera.CameraData
import io.reactivex.rxjava3.core.Observable

/**
 * @author LukeLorusso on 26-03-2021.
 */
internal class CameraViewModel(
        repository: ColorRepository,
        errorMessageFactory: ErrorMessageFactory
) : AViewModel<CameraData>(errorMessageFactory) {

    private val getColor = GetColor(repository)

    fun intentGetColor(param: GetColor.Param): Observable<CameraData> =
            getColor.execute(param)
                    .toObservable()
                    .map { CameraData.createColor(it) }
                    .doOnError { TimberWrapper.d { getErrorMessage(it) } }

}
