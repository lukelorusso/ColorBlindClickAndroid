package com.lukelorusso.presentation.ui.history

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
            .startWithSingle(HistoryData.createLoading())
            .onErrorReturn { onSnack(it) }

    internal fun intentRefreshData(param: Unit): Observable<HistoryData> = intentGetItems(param)
            .delay(200, TimeUnit.MILLISECONDS)
            .onErrorReturn { onSnack(it) }

    internal fun intentRetryData(param: Unit): Observable<HistoryData> = intentGetItems(param)
            .startWithSingle(HistoryData.createRetryLoading())
            .onErrorResumeNext(DelayFunction<HistoryData>(scheduler))
            .onErrorReturn { onSnack(it) }

    internal fun intentDeleteItem(param: Color): Observable<HistoryData> =
            deleteColor.execute(param)
                    .toSingleDefault(Unit)
                    .toObservable()
                    .map { HistoryData.createDeletedItem(param) }
                    .delay(450, TimeUnit.MILLISECONDS)
                    .startWithSingle(HistoryData.createLoading())
                    .onErrorReturn { onSnack(it) }

    internal fun intentDeleteAllItems(param: Unit): Observable<HistoryData> =
            deleteAllColors.execute(param)
                    .toSingleDefault(Unit)
                    .toObservable()
                    .map { HistoryData.createDeletedAllItem() }
                    .delay(450, TimeUnit.MILLISECONDS)
                    .startWithSingle(HistoryData.createLoading())
                    .onErrorReturn { onSnack(it) }

    internal fun gotoCamera() = router.routeToCamera()

    internal fun gotoPreview(color: Color) = router.routeToPreview(color)

    private fun onSnack(error: Throwable): HistoryData =
            HistoryData.createSnack(getErrorMessage(error), error is PersistenceException)

}
