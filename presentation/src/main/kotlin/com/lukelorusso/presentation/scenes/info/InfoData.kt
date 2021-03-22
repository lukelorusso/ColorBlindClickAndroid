package com.lukelorusso.presentation.scenes.info

data class InfoData(
        val url: String? = null,
        val snackMessage: String? = null
) {

    companion object {
        fun createUrlToGoTo(url: String) = InfoData(url = url)

        fun createSnack(snackMessage: String) = InfoData(snackMessage = snackMessage)
    }

}
