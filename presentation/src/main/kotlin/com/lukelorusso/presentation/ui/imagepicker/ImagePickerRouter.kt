package com.lukelorusso.presentation.ui.imagepicker

import com.lukelorusso.presentation.ui.base.AppRouter

class ImagePickerRouter : AppRouter() {

    fun routeToPreview(serializedColor: String) = (activity as? ImagePickerActivity)?.showColorPreviewDialog(serializedColor)

}
