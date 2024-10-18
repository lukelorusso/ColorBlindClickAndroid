package com.lukelorusso.presentation.ui.history

import com.google.gson.Gson
import com.lukelorusso.data.extensions.startWithSingle
import com.lukelorusso.domain.exception.PersistenceException
import com.lukelorusso.domain.functions.DelayFunction
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.usecase.DeleteAllColorsUseCase
import com.lukelorusso.domain.usecase.DeleteColorUseCase
import com.lukelorusso.domain.usecase.GetColorListUseCase
import com.lukelorusso.domain.usecase.base.UseCaseScheduler
import com.lukelorusso.presentation.exception.ErrorMessageFactory
import com.lukelorusso.presentation.ui.base.AViewModel
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class HistoryViewModel(
    private val gson: Gson,
    private val getColorList: GetColorListUseCase,
    private val deleteColor: DeleteColorUseCase,
    private val deleteAllColors: DeleteAllColorsUseCase,
    private val scheduler: UseCaseScheduler,
    override val router: HistoryRouter,
    errorMessageFactory: ErrorMessageFactory
) : AViewModel<HistoryData>(errorMessageFactory) {

    private fun intentGetItems(param: Unit): Observable<HistoryData> = getColorList.execute(param)
        .toObservable()
        .map { HistoryData.createData(it) }

    internal fun intentLoadData(param: Unit): Observable<HistoryData> = intentGetItems(param)
        //.startWithSingle(HistoryData.createLoading())
        .onErrorReturn { onError(it) }

    internal fun intentRefreshData(param: Unit): Observable<HistoryData> = intentGetItems(param)
        .delay(200, TimeUnit.MILLISECONDS)
        .onErrorReturn { onError(it) }

    internal fun intentRetryData(param: Unit): Observable<HistoryData> = intentGetItems(param)
        .startWithSingle(HistoryData.createRetryLoading())
        .onErrorResumeNext(DelayFunction<HistoryData>(scheduler))
        .onErrorReturn { onError(it) }

    internal fun intentDeleteItem(param: Color): Observable<HistoryData> =
        deleteColor.execute(param)
            .toSingleDefault(Unit)
            .toObservable()
            .map { HistoryData.createDeletedItem(param) }
            .delay(450, TimeUnit.MILLISECONDS)
            .startWithSingle(HistoryData.createLoading())
            .onErrorReturn { onError(it) }

    internal fun intentDeleteAllItems(param: Unit): Observable<HistoryData> =
        deleteAllColors.execute(param)
            .toSingleDefault(Unit)
            .toObservable()
            .map { HistoryData.createDeletedAllItem() }
            .delay(450, TimeUnit.MILLISECONDS)
            .startWithSingle(HistoryData.createLoading())
            .onErrorReturn { onError(it) }

    internal fun gotoCamera() = router.routeToCamera()

    internal fun gotoPreview(color: Color) = router.routeToPreview(gson.toJson(color))

    private fun onError(e: Throwable): HistoryData =
        HistoryData.createIsPersistenceException(e is PersistenceException)
            .also { postEvent(getErrorMessage(e)) }

}
