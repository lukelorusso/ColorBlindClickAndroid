package com.lukelorusso.presentation.scenes.preview

data class PreviewDialogViewModel(
    val homeUrl: String? = null,
    val snackMessage: String? = null
) {

    companion object {
        fun createHomeUrl(homeUrl: String) = PreviewDialogViewModel(homeUrl = homeUrl)

        fun createSnack(snackMessage: String) = PreviewDialogViewModel(snackMessage = snackMessage)
    }

}
