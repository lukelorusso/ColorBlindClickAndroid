package com.lukelorusso.presentation.ui.v3.history

import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.ui.main.MainActivity
import com.lukelorusso.presentation.ui.v3.base.AppRouter

class HistoryRouter : AppRouter() {

    fun routeToPreview(color: Color) = (activity as? MainActivity)?.showColorPreviewDialog(color)

    fun routeToCamera() = (activity as? MainActivity)?.gotoCamera()

}
