package com.lukelorusso.presentation.ui.camera

import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.ui.base.ARouter
import com.lukelorusso.presentation.ui.main.MainActivity

class CameraRouter : ARouter() {

    fun routeToInfo() = (activity as? MainActivity)?.gotoInfo()

    fun routeToHistory() = (activity as? MainActivity)?.gotoHistory()

    fun routeToPreview(color: Color) = (activity as? MainActivity)?.showColorPreviewDialog(color)

}
