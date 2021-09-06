package com.lukelorusso.presentation.ui.settings

import com.lukelorusso.presentation.exception.ErrorMessageFactory
import com.lukelorusso.presentation.ui.base.AViewModel

class SettingsViewModel(
    errorMessageFactory: ErrorMessageFactory
) : AViewModel<SettingsData>(errorMessageFactory) {

    /*internal fun intentGetHomeUrl(param: Unit): Observable<SettingsData> =
        getHomeUrl.execute(param)
            .toObservable()
            .map { SettingsData.createHomeUrl(it) }
            .onErrorReturn { onError(it) }*/

    private fun onError(e: Throwable): SettingsData =
        SettingsData.createEmptyContent().also { postEvent(getErrorMessage(e)) }

}
