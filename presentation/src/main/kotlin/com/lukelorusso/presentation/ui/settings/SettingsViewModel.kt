package com.lukelorusso.presentation.ui.settings

import com.lukelorusso.domain.usecase.GetPixelNeighbourhoodUseCase
import com.lukelorusso.domain.usecase.SetPixelNeighbourhoodUseCase
import com.lukelorusso.presentation.exception.ErrorMessageFactory
import com.lukelorusso.presentation.ui.base.AViewModel
import io.reactivex.rxjava3.core.Observable

class SettingsViewModel(
    private val getPixelNeighbourhood: GetPixelNeighbourhoodUseCase,
    private val setPixelNeighbourhood: SetPixelNeighbourhoodUseCase,
    errorMessageFactory: ErrorMessageFactory
) : AViewModel<SettingsData>(errorMessageFactory) {

    internal fun intentLoadData(param: Unit): Observable<SettingsData> =
        getPixelNeighbourhood.execute(param)
            .toObservable()
            .map { SettingsData.createContent(it) }
            .onErrorReturn { onError(it) }

    internal fun intentSetPixelNeighbourhood(param: Int): Observable<SettingsData> =
        setPixelNeighbourhood.execute(param)
            .toSingleDefault(Unit)
            .toObservable()
            .map { SettingsData.createEmptyContent() }
            .onErrorReturn { onError(it) }

    private fun onError(e: Throwable): SettingsData =
        SettingsData.createEmptyContent().also { postEvent(getErrorMessage(e)) }

}
