package com.lukelorusso.presentation.scenes.history

import androidx.appcompat.app.AppCompatActivity
import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.scenes.main.MainActivity
import javax.inject.Inject

class HistoryRouter
@Inject internal constructor(private val activity: AppCompatActivity) {

    fun routeToCamera() {
        if (activity is MainActivity) {
            activity.gotoCamera()
        }
    }

    fun routeToPreview(color: Color) {
        if (activity is MainActivity) {
            activity.showColorPreviewDialog(color)
        }
    }
}
