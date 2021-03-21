package com.lukelorusso.presentation_old.scenes.preview

import com.lukelorusso.presentation_old.scenes.base.view.LoadDataView
import io.reactivex.rxjava3.core.Observable

interface PreviewDialogView : LoadDataView<PreviewDialogViewModel> {

    fun intentGetHomeUrl(): Observable<Unit>

}
