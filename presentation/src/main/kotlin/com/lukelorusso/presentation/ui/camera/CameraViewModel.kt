package com.lukelorusso.presentation.ui.camera

import com.lukelorusso.data.extensions.startWithSingle
import com.lukelorusso.domain.exception.PersistenceException
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.usecase.GetColorUseCase
import com.lukelorusso.domain.usecase.GetHomeUrlUseCase
import com.lukelorusso.domain.usecase.GetLastLensPositionUseCase
import com.lukelorusso.domain.usecase.SetLastLensPositionUseCase
import com.lukelorusso.presentation.exception.ErrorMessageFactory
import com.lukelorusso.presentation.ui.base.AViewModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class CameraViewModel
@Inject constructor(
    private val getHomeUrl: GetHomeUrlUseCase,
    private val getLastLensPosition: GetLastLensPositionUseCase,
    private val setLastLensPosition: SetLastLensPositionUseCase,
    private val getColor: GetColorUseCase,
    override val router: CameraRouter,
    errorMessageFactory: ErrorMessageFactory
) : AViewModel<CameraData>(errorMessageFactory) {

    internal fun intentGetColor(param: GetColorUseCase.Param): Observable<CameraData> =
        getColor.execute(param)
            .toObservable()
            .map { CameraData.createColor(it) }
            .startWithSingle(CameraData.createLoading())
            .onErrorReturn { onSnack(it) }

    internal fun intentGetHomeUrl(param: Unit): Observable<CameraData> = getHomeUrl.execute(param)
        .toObservable()
        .map { CameraData.createHomeUrl(it) }
        .onErrorReturn { onSnack(it) }

    internal fun intentGetLastLensPosition(param: Unit): Observable<CameraData> =
        getLastLensPosition.execute(param)
            .toObservable()
            .map { CameraData.createLastLensPosition(it) }
            .onErrorReturn { onSnack(it) }

    internal fun intentSetLastLensPosition(param: Int): Observable<CameraData> =
        setLastLensPosition.execute(param)
            .toSingleDefault(Unit)
            .toObservable()
            .map { CameraData.createContentOnly() }
            .onErrorReturn { onSnack(it) }

    internal fun gotoInfo() = router.routeToInfo()

    internal fun gotoHistory() = router.routeToHistory()

    internal fun gotoPreview(color: Color) = router.routeToPreview(color)

    private fun onSnack(error: Throwable): CameraData =
        CameraData.createSnack(getErrorMessage(error), error is PersistenceException)

}
