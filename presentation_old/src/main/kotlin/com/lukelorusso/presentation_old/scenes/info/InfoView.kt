package com.lukelorusso.presentation_old.scenes.info

import com.lukelorusso.presentation_old.scenes.base.view.LoadDataView
import io.reactivex.rxjava3.core.Observable

interface InfoView : LoadDataView<InfoViewModel> {

    fun intentGotoHome(): Observable<Unit>

    fun intentGotoHelp(): Observable<Unit>

    fun intentGotoAboutMe(): Observable<Unit>

    fun intentOpenBrowser(): Observable<String>

    fun intentGotoCamera(): Observable<Unit>

}
