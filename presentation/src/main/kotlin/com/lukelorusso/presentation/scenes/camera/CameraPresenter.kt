package com.lukelorusso.presentation.scenes.camera

import com.lukelorusso.data.extensions.startWithSingle
import com.lukelorusso.domain.exception.PersistenceException
import com.lukelorusso.domain.usecases.GetColor
import com.lukelorusso.domain.usecases.GetHomeUrl
import com.lukelorusso.domain.usecases.GetLastLensPosition
import com.lukelorusso.domain.usecases.SetLastLensPosition
import com.lukelorusso.presentation.exception.ErrorMessageFactory
import com.lukelorusso.presentation.scenes.base.presenter.APresenter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class CameraPresenter
@Inject constructor(
    private val getHomeUrl: GetHomeUrl,
    private val getLastLensPosition: GetLastLensPosition,
    private val setLastLensPosition: SetLastLensPosition,
    private val getColor: GetColor,
    private val router: CameraRouter,
    errorMessageFactory: ErrorMessageFactory
) : APresenter<CameraView, CameraViewModel>(errorMessageFactory) {

    override fun attach(view: CameraView) {
        val loadData = view.intentLoadData().flatMap { unit ->
            Observable.merge(getHomeUrl(unit), getLastLensPosition(unit))
        }
        val getColor = view.intentGetColor().flatMap { getColor(it) }
        val setLastLensPosition = view.intentSetLastLensPosition()
            .flatMap { setLastLensPosition(it) }

        subscribeViewModel(view, getColor, loadData, setLastLensPosition)

        view.intentGotoInfo()
            .subscribe { router.routeToInfo() }
            .addTo(composite)

        view.intentGotoHistory()
            .subscribe { router.routeToHistory() }
            .addTo(composite)

        view.intentOpenPreview()
            .subscribe { router.routeToPreview(it) }
            .addTo(composite)
    }

    //region USE CASES TO VIEW MODEL
    private fun getColor(param: Pair<String, String>): Observable<CameraViewModel> =
        getColor.execute(param).toObservable()
            .map { CameraViewModel.createColor(it) }
            .startWithSingle(CameraViewModel.createLoading())
            .onErrorReturn { onSnack(it) }

    private fun getHomeUrl(param: Unit): Observable<CameraViewModel> =
        getHomeUrl.execute(param).toObservable()
            .map { CameraViewModel.createHomeUrl(it) }
            .onErrorReturn { onSnack(it) }

    private fun getLastLensPosition(param: Unit): Observable<CameraViewModel> =
        getLastLensPosition.execute(param).toObservable()
            .map { CameraViewModel.createLastLensPosition(it) }
            .onErrorReturn { onSnack(it) }

    private fun setLastLensPosition(param: Int): Observable<CameraViewModel> =
        setLastLensPosition.execute(param).toSingleDefault(Unit).toObservable()
            .map { CameraViewModel.createContentOnly() }
            .onErrorReturn { onSnack(it) }
    //endregion

    private fun onSnack(error: Throwable): CameraViewModel =
        CameraViewModel.createSnack(getErrorMessage(error), error is PersistenceException)

}
