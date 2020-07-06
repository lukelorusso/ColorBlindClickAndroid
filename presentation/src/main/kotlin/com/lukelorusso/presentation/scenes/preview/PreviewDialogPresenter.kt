package com.lukelorusso.presentation.scenes.preview

import com.lukelorusso.domain.usecases.GetHomeUrl
import com.lukelorusso.presentation.exception.ErrorMessageFactory
import com.lukelorusso.presentation.scenes.base.presenter.APresenter
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class PreviewDialogPresenter
@Inject constructor(
    private val getHomeUrl: GetHomeUrl,
    errorMessageFactory: ErrorMessageFactory
) : APresenter<PreviewDialogView, PreviewDialogViewModel>(errorMessageFactory) {

    override fun attach(view: PreviewDialogView) {
        val getHomeUrl = view.intentGetHomeUrl().flatMap { getHomeUrl(it) }

        subscribeViewModel(view, getHomeUrl)
    }

    //region USE CASES TO VIEW MODEL

    private fun getHomeUrl(param: Unit): Observable<PreviewDialogViewModel> =
        getHomeUrl.execute(param).toObservable()
            .map { PreviewDialogViewModel.createHomeUrl(it) }
            .onErrorReturn { onSnack(it) }
    //endregion

    private fun onSnack(error: Throwable): PreviewDialogViewModel =
        PreviewDialogViewModel.createSnack(getErrorMessage(error))

}
