package com.lukelorusso.colorblindclick.presentation.ui.history

import com.lukelorusso.colorblindclick.presentation.ui.base.AppRouter
import com.lukelorusso.colorblindclick.presentation.ui.main.MainActivity

class HistoryRouter : AppRouter() {

    fun routeToPreview(serializedColor: String) = (activity as? MainActivity)?.showColorPreviewDialog(serializedColor)

    fun routeToCamera() = (activity as? MainActivity)?.gotoCamera()

}
