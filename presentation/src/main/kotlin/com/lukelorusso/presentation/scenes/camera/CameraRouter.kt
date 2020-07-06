package com.lukelorusso.presentation.scenes.camera

import androidx.appcompat.app.AppCompatActivity
import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.scenes.main.MainActivity
import javax.inject.Inject

class CameraRouter
@Inject internal constructor(private val activity: AppCompatActivity) {

    fun routeToInfo() {
        if (activity is MainActivity) {
            activity.gotoInfo()
        }
    }

    fun routeToHistory() {
        if (activity is MainActivity) {
            activity.gotoHistory()
        }
    }

    fun routeToPreview(color: Color) {
        if (activity is MainActivity) {
            activity.showColorPreviewDialog(color)
        }
    }

}
