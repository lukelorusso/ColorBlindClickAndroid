package com.lukelorusso.presentation.ui.preview

import com.lukelorusso.domain.usecase.GetHomeUrlUseCase
import com.lukelorusso.presentation.exception.ErrorMessageFactory
import com.lukelorusso.presentation.ui.base.AViewModel
import io.reactivex.rxjava3.core.Observable

class PreviewDialogViewModel(
    private val getHomeUrl: GetHomeUrlUseCase,
    errorMessageFactory: ErrorMessageFactory
) : AViewModel<PreviewDialogData>(errorMessageFactory) {

    internal fun intentGetHomeUrl(param: Unit): Observable<PreviewDialogData> =
        getHomeUrl.execute(param)
            .toObservable()
            .map { PreviewDialogData.createHomeUrl(it) }
            .onErrorReturn { onSnack(it) }

    private fun onSnack(error: Throwable): PreviewDialogData =
        PreviewDialogData.createSnack(getErrorMessage(error))

}
