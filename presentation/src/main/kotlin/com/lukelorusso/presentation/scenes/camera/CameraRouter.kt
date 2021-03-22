package com.lukelorusso.presentation.scenes.camera

import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.scenes.base.router.ARouter
import com.lukelorusso.presentation.scenes.main.MainActivity
import javax.inject.Inject

class CameraRouter
@Inject internal constructor() : ARouter() {

    fun routeToInfo() = (activity as? MainActivity)?.gotoInfo()

    fun routeToHistory() = (activity as? MainActivity)?.gotoHistory()

    fun routeToPreview(color: Color) = (activity as? MainActivity)?.showColorPreviewDialog(color)

}
