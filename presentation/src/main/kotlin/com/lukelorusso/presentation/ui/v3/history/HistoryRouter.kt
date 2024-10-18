package com.lukelorusso.presentation.ui.v3.history

import com.lukelorusso.presentation.ui.main.MainActivity
import com.lukelorusso.presentation.ui.v3.base.AppRouter

class HistoryRouter : AppRouter() {

    fun routeToPreview(serializedColor: String) = (activity as? MainActivity)?.showColorPreviewDialog(serializedColor)

    fun routeToCamera() = (activity as? MainActivity)?.gotoCamera()

}
