package com.lukelorusso.presentation_old.scenes.info

import androidx.appcompat.app.AppCompatActivity
import com.lukelorusso.presentation_old.extensions.redirectToBrowser
import com.lukelorusso.presentation_old.scenes.main.MainActivity
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
