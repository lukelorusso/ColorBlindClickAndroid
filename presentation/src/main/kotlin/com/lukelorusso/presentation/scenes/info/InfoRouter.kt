package com.lukelorusso.presentation.scenes.info

import androidx.appcompat.app.AppCompatActivity
import com.lukelorusso.presentation.extensions.redirectToBrowser
import com.lukelorusso.presentation.scenes.main.MainActivity
import javax.inject.Inject

class InfoRouter
@Inject internal constructor(private val activity: AppCompatActivity) {

    fun routeToBrowser(url: String) = activity.redirectToBrowser(url)

    fun routeToCamera() {
        if (activity is MainActivity) {
            activity.gotoCamera()
        }
    }

}
