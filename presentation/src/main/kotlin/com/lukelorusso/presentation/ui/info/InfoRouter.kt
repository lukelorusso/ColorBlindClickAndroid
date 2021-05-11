package com.lukelorusso.presentation.ui.info

import com.lukelorusso.presentation.extensions.redirectToBrowser
import com.lukelorusso.presentation.ui.base.ARouter
import com.lukelorusso.presentation.ui.main.MainActivity

class InfoRouter : ARouter() {

    fun routeToBrowser(url: String) = activity?.redirectToBrowser(url)

    fun routeToCamera() = (activity as? MainActivity)?.gotoCamera()

}
