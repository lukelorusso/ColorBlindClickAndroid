package com.lukelorusso.presentation.ui.capture

import android.net.Uri
import com.lukelorusso.presentation.ui.base.AppRouter
import com.lukelorusso.presentation.ui.imagepicker.ImagePickerActivity
import com.lukelorusso.presentation.ui.main.MainActivity

class CaptureRouter : AppRouter() {

    fun routeToInfo() = (activity as? MainActivity)?.gotoInfo()

    fun routeToHistory() = (activity as? MainActivity)?.gotoHistory()

    fun routeToPreview(serializedColor: String) = (activity as? MainActivity)?.showColorPreviewDialog(serializedColor)

    fun routeToImagePicker(uri: Uri) = (activity as? MainActivity)?.let { mainActivity ->
        val intent = ImagePickerActivity.newIntent(mainActivity, uri)
        mainActivity.startActivity(intent)
    }

}
