package com.lukelorusso.presentation.scenes.preview

import com.lukelorusso.presentation.scenes.base.view.LoadDataView
import io.reactivex.rxjava3.core.Observable

interface PreviewDialogView : LoadDataView<PreviewDialogViewModel> {

    fun intentGetHomeUrl(): Observable<Unit>

}
