package com.lukelorusso.presentation_old.scenes.history

import com.lukelorusso.data.extensions.startWithSingle
import com.lukelorusso.domain.exception.PersistenceException
import com.lukelorusso.domain.functions.DelayFunction
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.usecases.DeleteAllColors
import com.lukelorusso.domain.usecases.DeleteColor
import com.lukelorusso.domain.usecases.GetColorList
import com.lukelorusso.presentation_old.exception.ErrorMessageFactory
import com.lukelorusso.presentation_old.scenes.base.presenter.APresenter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HistoryPresenter
@Inject constructor(
    private val getColorList: GetColorList,
    private val deleteColor: DeleteColor,
    private val deleteAllColors: DeleteAllColors,
    private val scheduler: Scheduler,
    private val router: HistoryRouter,
    errorMessageFactory: ErrorMessageFactory
) : APresenter<HistoryView, HistoryViewModel>(errorMessageFactory) {

    override fun attach(view: HistoryView) {
        val loadData = view.intentLoadData().flatMap { loadData(it) }
        val refreshData = view.intentRefreshData().flatMap { refreshData(it) }
        val retryData = view.intentErrorRetry().flatMap { retryData(it) }
        val deleteItem = view.intentDeleteItem().flatMap { deleteItem(it) }
        val deleteAllItem = view.intentDeleteAllItem().flatMap { deleteAllItems(it) }

        subscribeViewModel(view, loadData, refreshData, retryData, deleteItem, deleteAllItem)

        view.intentGotoCamera()
            .subscribe { router.routeToCamera() }
            .addTo(composite)

        view.intentOpenPreview()
            .subscribe { router.routeToPreview(it) }
            .addTo(composite)
    }

    //region USE CASES TO VIEW MODEL
    private fun getItems(param: Unit): Observable<HistoryViewModel> =
        getColorList.execute(param).toObservable()
            .map { HistoryViewModel.createData(it) }

    private fun loadData(param: Unit): Observable<HistoryViewModel> =
        getItems(param)
            .startWithSingle(HistoryViewModel.createLoading())
            .onErrorReturn { onSnack(it) }

    private fun refreshData(param: Unit): Observable<HistoryViewModel> =
        getItems(param)
            .delay(200, TimeUnit.MILLISECONDS)
            .onErrorReturn { onSnack(it) }

    private fun retryData(param: Unit): Observable<HistoryViewModel> =
        getItems(param)
            .startWithSingle(HistoryViewModel.createRetryLoading())
            .onErrorResumeNext(DelayFunction<HistoryViewModel>(scheduler))
            .onErrorReturn { onSnack(it) }

    private fun deleteItem(param: Color): Observable<HistoryViewModel> =
        deleteColor.execute(param).toSingleDefault(Unit).toObservable()
            .map { HistoryViewModel.createDeletedItem(param) }
            .delay(450, TimeUnit.MILLISECONDS)
            .startWithSingle(HistoryViewModel.createLoading())
            .onErrorReturn { onSnack(it) }

    private fun deleteAllItems(param: Unit): Observable<HistoryViewModel> =
        deleteAllColors.execute(param).toSingleDefault(Unit).toObservable()
            .map { HistoryViewModel.createDeletedAllItem() }
            .delay(450, TimeUnit.MILLISECONDS)
            .startWithSingle(HistoryViewModel.createLoading())
            .onErrorReturn { onSnack(it) }
    //endregion

    private fun onSnack(error: Throwable): HistoryViewModel =
        HistoryViewModel.createSnack(getErrorMessage(error), error is PersistenceException)

}
