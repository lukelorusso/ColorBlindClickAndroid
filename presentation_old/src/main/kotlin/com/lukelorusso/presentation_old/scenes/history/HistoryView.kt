package com.lukelorusso.presentation_old.scenes.history

import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation_old.scenes.base.view.LoadDataView
import io.reactivex.rxjava3.core.Observable

interface HistoryView : LoadDataView<HistoryViewModel> {

    fun intentLoadData(): Observable<Unit>

    fun intentRefreshData(): Observable<Unit>

    fun intentErrorRetry(): Observable<Unit>

    fun intentDeleteItem(): Observable<Color>

    fun intentDeleteAllItem(): Observable<Unit>

    fun intentGotoCamera(): Observable<Unit>

    fun intentOpenPreview(): Observable<Color>

}
