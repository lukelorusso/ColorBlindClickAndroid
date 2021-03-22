package com.lukelorusso.presentation.scenes.info

import com.lukelorusso.presentation.extensions.redirectToBrowser
import com.lukelorusso.presentation.scenes.base.router.ARouter
import com.lukelorusso.presentation.scenes.main.MainActivity
import javax.inject.Inject

class InfoRouter
@Inject internal constructor() : ARouter() {

    fun routeToBrowser(url: String) = activity?.redirectToBrowser(url)

    fun routeToCamera() = (activity as? MainActivity)?.gotoCamera()

}
