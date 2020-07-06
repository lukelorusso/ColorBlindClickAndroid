package com.lukelorusso.presentation.scenes.info

import com.lukelorusso.presentation.scenes.base.view.LoadDataView
import io.reactivex.rxjava3.core.Observable

interface InfoView : LoadDataView<InfoViewModel> {

    fun intentGotoHome(): Observable<Unit>

    fun intentGotoHelp(): Observable<Unit>

    fun intentGotoAboutMe(): Observable<Unit>

    fun intentOpenBrowser(): Observable<String>

    fun intentGotoCamera(): Observable<Unit>

}
