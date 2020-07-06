package com.lukelorusso.presentation.scenes.info

import com.lukelorusso.domain.usecases.GetAboutMeUrl
import com.lukelorusso.domain.usecases.GetHelpUrl
import com.lukelorusso.domain.usecases.GetHomeUrl
import com.lukelorusso.presentation.exception.ErrorMessageFactory
import com.lukelorusso.presentation.scenes.base.presenter.APresenter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class InfoPresenter
@Inject constructor(
    private val getHelpUrl: GetHelpUrl,
    private val getHomeUrl: GetHomeUrl,
    private val getAboutMeUrl: GetAboutMeUrl,
    private val router: InfoRouter,
    errorMessageFactory: ErrorMessageFactory
) : APresenter<InfoView, InfoViewModel>(errorMessageFactory) {

    override fun attach(view: InfoView) {
        val getHelpUrl = view.intentGotoHelp().flatMap { getHelpUrl(it) }
        val getHomeUrl = view.intentGotoHome().flatMap { getHomeUrl(it) }
        val getAboutMeUrl = view.intentGotoAboutMe().flatMap { getAboutMeUrl(it) }

        subscribeViewModel(view, getHelpUrl, getHomeUrl, getAboutMeUrl)

        view.intentOpenBrowser()
            .subscribe { router.routeToBrowser(it) }
            .addTo(composite)

        view.intentGotoCamera()
            .subscribe { router.routeToCamera() }
            .addTo(composite)
    }

    //region USE CASES TO VIEW MODEL
    private fun getHelpUrl(param: Unit): Observable<InfoViewModel> =
        getHelpUrl.execute(param).toObservable()
            .map { InfoViewModel.createUrlToGoTo(it) }
            .onErrorReturn { onSnack(it) }

    private fun getHomeUrl(param: Unit): Observable<InfoViewModel> =
        getHomeUrl.execute(param).toObservable()
            .map { InfoViewModel.createUrlToGoTo(it) }
            .onErrorReturn { onSnack(it) }

    private fun getAboutMeUrl(param: Unit): Observable<InfoViewModel> =
        getAboutMeUrl.execute(param).toObservable()
            .map { InfoViewModel.createUrlToGoTo(it) }
            .onErrorReturn { onSnack(it) }
    //endregion

    private fun onSnack(error: Throwable): InfoViewModel =
        InfoViewModel.createSnack(getErrorMessage(error))

}
