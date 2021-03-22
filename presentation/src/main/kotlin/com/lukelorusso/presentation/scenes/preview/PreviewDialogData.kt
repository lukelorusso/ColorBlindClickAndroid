package com.lukelorusso.presentation.scenes.preview

data class PreviewDialogData(
        val homeUrl: String? = null,
        val snackMessage: String? = null
) {

    companion object {
        fun createHomeUrl(homeUrl: String) = PreviewDialogData(homeUrl = homeUrl)

        fun createSnack(snackMessage: String) = PreviewDialogData(snackMessage = snackMessage)
    }

}
