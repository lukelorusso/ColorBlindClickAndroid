package com.lukelorusso.presentation.ui.preview

import com.lukelorusso.domain.usecase.GetHomeUrlUseCase
import com.lukelorusso.presentation.exception.ErrorMessageFactory
import com.lukelorusso.presentation.ui.base.AViewModel
import io.reactivex.rxjava3.core.Observable

class PreviewViewModel(
    private val getHomeUrl: GetHomeUrlUseCase,
    errorMessageFactory: ErrorMessageFactory
) : AViewModel<PreviewData>(errorMessageFactory) {

    internal fun intentGetHomeUrl(param: Unit): Observable<PreviewData> =
        getHomeUrl.execute(param)
            .toObservable()
            .map { PreviewData.createHomeUrl(it) }
            .onErrorReturn { onError(it) }

    private fun onError(e: Throwable): PreviewData =
        PreviewData.createEmptyContent().also { postEvent(getErrorMessage(e)) }

}
