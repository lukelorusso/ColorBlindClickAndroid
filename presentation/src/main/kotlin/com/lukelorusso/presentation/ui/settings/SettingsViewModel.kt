package com.lukelorusso.presentation.ui.settings

import com.lukelorusso.domain.usecase.GetPixelNeighbourhoodUseCase
import com.lukelorusso.domain.usecase.GetSaveCameraOptionsUseCase
import com.lukelorusso.domain.usecase.SetPixelNeighbourhoodUseCase
import com.lukelorusso.domain.usecase.SetSaveCameraOptionsUseCase
import com.lukelorusso.presentation.exception.ErrorMessageFactory
import com.lukelorusso.presentation.ui.base.AViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class SettingsViewModel(
    private val getPixelNeighbourhood: GetPixelNeighbourhoodUseCase,
    private val setPixelNeighbourhood: SetPixelNeighbourhoodUseCase,
    private val getSaveCameraOptions: GetSaveCameraOptionsUseCase,
    private val setSaveCameraOptions: SetSaveCameraOptionsUseCase,
    errorMessageFactory: ErrorMessageFactory
) : AViewModel<SettingsData>(errorMessageFactory) {

    internal fun intentLoadData(param: Unit): Observable<SettingsData> =
        Single.zip(
            getPixelNeighbourhood.execute(param),
            getSaveCameraOptions.execute(param),
            { pixelNeighbourhood, saveCameraOption ->
                SettingsData.createContent(
                    pixelNeighbourhood = pixelNeighbourhood,
                    saveCameraOption = saveCameraOption
                )
            }
        ).toObservable().onErrorReturn { onError(it) }

    internal fun intentSetPixelNeighbourhood(param: Int): Observable<SettingsData> =
        setPixelNeighbourhood.execute(param)
            .toSingleDefault(Unit)
            .toObservable()
            .map { SettingsData.createEmptyContent() }
            .onErrorReturn { onError(it) }

    internal fun intentSetSaveCameraOption(param: Boolean): Observable<SettingsData> =
        setSaveCameraOptions.execute(param)
            .toSingleDefault(Unit)
            .toObservable()
            .map { SettingsData.createEmptyContent() }
            .onErrorReturn { onError(it) }

    private fun onError(e: Throwable): SettingsData =
        SettingsData.createEmptyContent().also { postEvent(getErrorMessage(e)) }

}
