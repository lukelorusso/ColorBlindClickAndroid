package com.lukelorusso.presentation.scenes.history

import com.lukelorusso.data.extensions.startWithSingle
import com.lukelorusso.domain.exception.PersistenceException
import com.lukelorusso.domain.functions.DelayFunction
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.usecases.DeleteAllColors
import com.lukelorusso.domain.usecases.DeleteColor
import com.lukelorusso.domain.usecases.GetColorList
import com.lukelorusso.presentation.exception.ErrorMessageFactory
import com.lukelorusso.presentation.scenes.base.viewmodel.AViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HistoryViewModel
@Inject constructor(
        private val getColorList: GetColorList,
        private val deleteColor: DeleteColor,
        private val deleteAllColors: DeleteAllColors,
        private val scheduler: Scheduler,
        private val router: HistoryRouter,
        errorMessageFactory: ErrorMessageFactory
) : AViewModel<HistoryData>(errorMessageFactory, router) {

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
