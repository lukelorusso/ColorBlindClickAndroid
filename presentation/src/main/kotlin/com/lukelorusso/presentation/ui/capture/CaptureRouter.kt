package com.lukelorusso.presentation.ui.capture

import com.lukelorusso.presentation.ui.base.AppRouter
import com.lukelorusso.presentation.ui.main.MainActivity

class CaptureRouter : AppRouter() {

    fun routeToInfo() = (activity as? MainActivity)?.gotoInfo()

    fun routeToHistory() = (activity as? MainActivity)?.gotoHistory()

    fun routeToPreview(serializedColor: String) = (activity as? MainActivity)?.showColorPreviewDialog(serializedColor)

}
