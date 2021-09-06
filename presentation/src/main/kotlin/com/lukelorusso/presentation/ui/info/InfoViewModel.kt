package com.lukelorusso.presentation.ui.info

import com.lukelorusso.domain.usecase.GetAboutMeUrlUseCase
import com.lukelorusso.domain.usecase.GetHelpUrlUseCase
import com.lukelorusso.domain.usecase.GetHomeUrlUseCase
import com.lukelorusso.presentation.exception.ErrorMessageFactory
import com.lukelorusso.presentation.ui.base.AViewModel
import io.reactivex.rxjava3.core.Observable

class InfoViewModel(
    private val getHelpUrl: GetHelpUrlUseCase,
    private val getHomeUrl: GetHomeUrlUseCase,
    private val getAboutMeUrl: GetAboutMeUrlUseCase,
    override val router: InfoRouter,
    errorMessageFactory: ErrorMessageFactory
) : AViewModel<InfoData>(errorMessageFactory) {

    internal fun intentGetHelpUrl(): Observable<InfoData> = getHelpUrl.execute(Unit)
        .toObservable()
        .map { InfoData.createUrlToGoTo(it) }
        .onErrorReturn { onError(it) }

    internal fun intentGetHomeUrl(): Observable<InfoData> = getHomeUrl.execute(Unit)
        .toObservable()
        .map { InfoData.createUrlToGoTo(it) }
        .onErrorReturn { onError(it) }

    internal fun intentGetAboutMeUrl(): Observable<InfoData> = getAboutMeUrl.execute(Unit)
        .toObservable()
        .map { InfoData.createUrlToGoTo(it) }
        .onErrorReturn { onError(it) }

    internal fun gotoBrowser(url: String) = router.routeToBrowser(url)

    internal fun gotoCamera() = router.routeToCamera()

    internal fun gotoSettings(): Observable<InfoData> = Observable.just(Unit)
        .map { InfoData.createEmptyContent() }
        .doAfterNext { router.routeToSettings() }

    private fun onError(e: Throwable): InfoData =
        InfoData.createEmptyContent().also { postEvent(getErrorMessage(e)) }

}
