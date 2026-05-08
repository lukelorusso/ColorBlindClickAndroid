package com.lukelorusso.colorblindclick.presentation.ui.info

import com.lukelorusso.colorblindclick.presentation.extensions.redirectToBrowser
import com.lukelorusso.colorblindclick.presentation.ui.base.AppRouter
import com.lukelorusso.colorblindclick.presentation.ui.main.MainActivity

class InfoRouter : AppRouter() {

    fun routeToBrowser(url: String) = activity?.redirectToBrowser(url)

    fun routeToSettings() = (activity as? MainActivity)?.showSettingsDialog()

    fun routeToCamera() = (activity as? MainActivity)?.gotoCamera()

}
