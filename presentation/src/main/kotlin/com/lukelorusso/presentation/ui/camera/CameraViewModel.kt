package com.lukelorusso.presentation.ui.camera

import com.google.gson.Gson
import com.lukelorusso.data.extensions.startWithSingle
import com.lukelorusso.domain.exception.PersistenceException
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.usecase.GetColorUseCase
import com.lukelorusso.domain.usecase.v3.*
import com.lukelorusso.presentation.exception.ErrorMessageFactory
import com.lukelorusso.presentation.ui.base.AViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.rx3.rxSingle
import javax.inject.Inject

class CameraViewModel
@Inject constructor(
    private val gson: Gson,
    private val getLastLensPosition: GetLastLensPositionUseCase,
    private val setLastLensPosition: SetLastLensPositionUseCase,
    private val getLastZoomValue: GetLastZoomValueUseCase,
    private val setLastZoomValue: SetLastZoomValueUseCase,
    private val getPixelNeighbourhood: GetPixelNeighbourhoodUseCase,
    private val getColor: GetColorUseCase,
    override val router: CameraRouter,
    errorMessageFactory: ErrorMessageFactory
) : AViewModel<CameraData>(errorMessageFactory) {

    internal fun intentLoadData(param: Unit): Observable<CameraData> =

        Single.zip(
            rxSingle { getLastLensPosition.invoke(param) },
            rxSingle { getLastZoomValue.invoke(param) },
            rxSingle { getPixelNeighbourhood.invoke(param) }
        ) { lensPosition, zoomValue, pixelNeighbourhood ->
            CameraData.createContent(
                lensPosition = lensPosition,
                zoomValue = zoomValue,
                pixelNeighbourhood = pixelNeighbourhood
            )
        }.toObservable().onErrorReturn { onError(it) }

    /**
     * param.first = LastLensPosition;
     * param.second = LastZoomValue
     */
    internal fun intentReloadData(param: Pair<Int, Int>): Observable<CameraData> =
        Single.zip(
            rxSingle { setLastLensPosition.invoke(param.first) },
            rxSingle { setLastZoomValue.invoke(param.second) },
            rxSingle { getPixelNeighbourhood.invoke(Unit) }
        ) { _, _, pixelNeighbourhood ->
            CameraData.createContent(pixelNeighbourhood = pixelNeighbourhood)
        }.toObservable().onErrorReturn { onError(it) }

    internal fun intentGetColor(param: GetColorUseCase.Param): Observable<CameraData> =
        getColor.execute(param)
            .toObservable()
            .map { CameraData.createColor(it) }
            .startWithSingle(CameraData.createLoading())
            .onErrorReturn { onError(it) }

    internal fun intentSetLastLensPosition(param: Int): Observable<CameraData> =
        rxSingle { setLastLensPosition.invoke(param) }
            .toObservable()
            .map { CameraData.createContent(zoomValue = -1) }
            .onErrorReturn { onError(it) }

    internal fun intentGetLastZoomValue(param: Unit): Observable<CameraData> =
        rxSingle { getLastZoomValue.invoke(param) }
            .toObservable()
            .map { CameraData.createContent(zoomValue = it) }
            .onErrorReturn { onError(it) }

    internal fun intentSetLastZoomValue(param: Int): Observable<CameraData> =
        rxSingle { setLastZoomValue.invoke(param) }
            .toObservable()
            .map { CameraData.createEmptyContent() }
            .onErrorReturn { onError(it) }

    internal fun intentGetPixelNeighbourhood(param: Unit): Observable<CameraData> =
        rxSingle { getPixelNeighbourhood.invoke(param) }
            .toObservable()
            .map { CameraData.createContent(pixelNeighbourhood = it) }
            .onErrorReturn { onError(it) }

    internal fun gotoInfo() = router.routeToInfo()

    internal fun gotoHistory() = router.routeToHistory()

    internal fun gotoPreview(color: Color) = router.routeToPreview(gson.toJson(color))

    private fun onError(e: Throwable): CameraData =
        CameraData.createIsPersistenceException(e is PersistenceException)
            .also { postEvent(getErrorMessage(e)) }

}
