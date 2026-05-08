package com.lukelorusso.colorblindclick.presentation.ui.imagepicker

import com.lukelorusso.colorblindclick.presentation.ui.base.AppRouter

class ImagePickerRouter : AppRouter() {

    fun routeToPreview(serializedColor: String) = (activity as? ImagePickerActivity)?.showColorPreviewDialog(serializedColor)

}
