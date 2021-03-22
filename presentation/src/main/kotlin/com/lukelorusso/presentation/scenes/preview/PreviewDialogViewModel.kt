package com.lukelorusso.presentation.scenes.preview

import com.lukelorusso.domain.usecases.GetHomeUrl
import com.lukelorusso.presentation.exception.ErrorMessageFactory
import com.lukelorusso.presentation.scenes.base.viewmodel.AViewModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class PreviewDialogViewModel
@Inject constructor(
        private val getHomeUrl: GetHomeUrl,
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
