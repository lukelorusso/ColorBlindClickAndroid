package com.lukelorusso.presentation.scenes.history

import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.scenes.base.router.ARouter
import com.lukelorusso.presentation.scenes.main.MainActivity
import javax.inject.Inject

class HistoryRouter
@Inject internal constructor() : ARouter() {

    fun routeToCamera() = (activity as? MainActivity)?.gotoCamera()

    fun routeToPreview(color: Color) = (activity as? MainActivity)?.showColorPreviewDialog(color)

}
