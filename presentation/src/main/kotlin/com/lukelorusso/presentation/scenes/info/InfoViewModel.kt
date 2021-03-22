package com.lukelorusso.presentation.scenes.info

import com.lukelorusso.domain.usecases.GetAboutMeUrl
import com.lukelorusso.domain.usecases.GetHelpUrl
import com.lukelorusso.domain.usecases.GetHomeUrl
import com.lukelorusso.presentation.exception.ErrorMessageFactory
import com.lukelorusso.presentation.scenes.base.viewmodel.AViewModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class InfoViewModel
@Inject constructor(
        private val getHelpUrl: GetHelpUrl,
        private val getHomeUrl: GetHomeUrl,
        private val getAboutMeUrl: GetAboutMeUrl,
        private val router: InfoRouter,
        errorMessageFactory: ErrorMessageFactory
) : AViewModel<InfoData>(errorMessageFactory, router) {

    internal fun intentGetHelpUrl(): Observable<InfoData> = getHelpUrl.execute(Unit)
            .toObservable()
            .map { InfoData.createUrlToGoTo(it) }
            .onErrorReturn { onSnack(it) }

    internal fun intentGetHomeUrl(): Observable<InfoData> = getHomeUrl.execute(Unit)
            .toObservable()
            .map { InfoData.createUrlToGoTo(it) }
            .onErrorReturn { onSnack(it) }

    internal fun intentGetAboutMeUrl(): Observable<InfoData> = getAboutMeUrl.execute(Unit)
            .toObservable()
            .map { InfoData.createUrlToGoTo(it) }
            .onErrorReturn { onSnack(it) }

    internal fun gotoBrowser(url: String) = router.routeToBrowser(url)

    internal fun gotoCamera() = router.routeToCamera()

    private fun onSnack(error: Throwable): InfoData =
            InfoData.createSnack(getErrorMessage(error))

}
