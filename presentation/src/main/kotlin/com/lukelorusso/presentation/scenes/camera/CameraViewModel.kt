package com.lukelorusso.presentation.scenes.camera

import com.lukelorusso.data.extensions.startWithSingle
import com.lukelorusso.domain.exception.PersistenceException
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.usecases.GetColor
import com.lukelorusso.domain.usecases.GetHomeUrl
import com.lukelorusso.domain.usecases.GetLastLensPosition
import com.lukelorusso.domain.usecases.SetLastLensPosition
import com.lukelorusso.presentation.exception.ErrorMessageFactory
import com.lukelorusso.presentation.scenes.base.viewmodel.AViewModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class CameraViewModel
@Inject constructor(
        private val getHomeUrl: GetHomeUrl,
        private val getLastLensPosition: GetLastLensPosition,
        private val setLastLensPosition: SetLastLensPosition,
        private val getColor: GetColor,
        private val router: CameraRouter,
        errorMessageFactory: ErrorMessageFactory
) : AViewModel<CameraData>(errorMessageFactory, router) {

    internal fun intentGetColor(param: GetColor.Param): Observable<CameraData> =
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
