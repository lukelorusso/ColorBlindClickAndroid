package com.lukelorusso.presentation.ui.info

import com.lukelorusso.presentation.extensions.redirectToBrowser
import com.lukelorusso.presentation.ui.main.MainActivity
import com.lukelorusso.presentation.ui.base.AppRouter

class InfoRouter : AppRouter() {

    fun routeToBrowser(url: String) = activity?.redirectToBrowser(url)

    fun routeToSettings() = (activity as? MainActivity)?.showSettingsDialog()

    fun routeToCamera() = (activity as? MainActivity)?.gotoCamera()

}
