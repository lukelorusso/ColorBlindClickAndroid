package com.lukelorusso.presentation_old.scenes.info

data class InfoViewModel(
    val url: String? = null,
    val snackMessage: String? = null
) {

    companion object {
        fun createUrlToGoTo(url: String) = InfoViewModel(url = url)

        fun createSnack(snackMessage: String) = InfoViewModel(snackMessage = snackMessage)
    }

}
