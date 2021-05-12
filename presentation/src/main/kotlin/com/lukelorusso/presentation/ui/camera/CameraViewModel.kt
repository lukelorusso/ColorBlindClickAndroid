package com.lukelorusso.presentation.ui.camera

import com.lukelorusso.data.extensions.startWithSingle
import com.lukelorusso.domain.exception.PersistenceException
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.usecase.*
import com.lukelorusso.presentation.exception.ErrorMessageFactory
import com.lukelorusso.presentation.ui.base.AViewModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class CameraViewModel
@Inject constructor(
    private val getHomeUrl: GetHomeUrlUseCase,
    private val getLastLensPosition: GetLastLensPositionUseCase,
    private val setLastLensPosition: SetLastLensPositionUseCase,
    private val getLastZoomValue: GetLastZoomValueUseCase,
    private val setLastZoomValue: SetLastZoomValueUseCase,
    private val getColor: GetColorUseCase,
    override val router: CameraRouter,
    errorMessageFactory: ErrorMessageFactory
) : AViewModel<CameraData>(errorMessageFactory) {

    internal fun intentGetColor(param: GetColorUseCase.Param): Observable<CameraData> =
        getColor.execute(param)
            .toObservable()
            .map { CameraData.createColor(it) }
            .startWithSingle(CameraData.createLoading())
            .onErrorReturn { onError(it) }

    internal fun intentGetHomeUrl(param: Unit): Observable<CameraData> = getHomeUrl.execute(param)
        .toObservable()
        .map { CameraData.createHomeUrl(it) }
        .onErrorReturn { onError(it) }

    internal fun intentGetLastLensPositionAndZoomValue(param: Unit): Observable<CameraData> =
        getLastLensPosition.execute(param).zipWith(
            getLastZoomValue.execute(param),
            { lensPosition, zoomValue ->
                CameraData.createContent(lensPosition = lensPosition, zoomValue = zoomValue)
            }
        ).toObservable().onErrorReturn { onError(it) }

    internal fun intentSetLastLensPosition(param: Int): Observable<CameraData> =
        setLastLensPosition.execute(param)
            .toSingleDefault(Unit)
            .toObservable()
            .map { CameraData.createContent(zoomValue = -1) }
            .onErrorReturn { onError(it) }

    internal fun intentGetLastZoomValue(param: Unit): Observable<CameraData> =
        getLastZoomValue.execute(param)
            .toObservable()
            .map { CameraData.createContent(zoomValue = it) }
            .onErrorReturn { onError(it) }

    internal fun intentSetLastZoomValue(param: Int): Observable<CameraData> =
        setLastZoomValue.execute(param)
            .toSingleDefault(Unit)
            .toObservable()
            .map { CameraData.createEmptyContent() }
            .onErrorReturn { onError(it) }

    internal fun gotoInfo() = router.routeToInfo()

    internal fun gotoHistory() = router.routeToHistory()

    internal fun gotoPreview(color: Color) = router.routeToPreview(color)

    private fun onError(e: Throwable): CameraData =
        CameraData.createIsPersistenceException(e is PersistenceException)
            .also { postEvent(getErrorMessage(e)) }

}
