package com.lukelorusso.presentation.ui.history

import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.ui.base.ARouter
import com.lukelorusso.presentation.ui.main.MainActivity

class HistoryRouter : ARouter() {

    fun routeToCamera() = (activity as? MainActivity)?.gotoCamera()

    fun routeToPreview(color: Color) = (activity as? MainActivity)?.showColorPreviewDialog(color)

}
