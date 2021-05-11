package com.lukelorusso.presentation.ui.preview

data class PreviewDialogData(
        val homeUrl: String? = null,
        val snackMessage: String? = null
) {

    companion object {
        fun createHomeUrl(homeUrl: String) = PreviewDialogData(homeUrl = homeUrl)

        fun createSnack(snackMessage: String) = PreviewDialogData(snackMessage = snackMessage)
    }

}
