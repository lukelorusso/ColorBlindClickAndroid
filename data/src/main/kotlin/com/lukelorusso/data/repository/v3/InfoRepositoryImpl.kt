package com.lukelorusso.data.repository.v3

import com.lukelorusso.data.net.RetrofitFactory.COLOR_BLIND_SITE_ABOUT_ME
import com.lukelorusso.data.net.RetrofitFactory.COLOR_BLIND_SITE_HELP
import com.lukelorusso.data.net.RetrofitFactory.COLOR_BLIND_SITE_HOME
import com.lukelorusso.domain.repository.v3.InfoRepository
import java.util.*

class InfoRepositoryImpl : InfoRepository {
    override fun getHelpUrl(): String {
        return String.format(
            COLOR_BLIND_SITE_HELP,
            getDeviceLanguage()
        )
    }

    override fun getHomeUrl(): String {
        return String.format(
            COLOR_BLIND_SITE_HOME,
            getDeviceLanguage()
        )
    }

    override fun getAboutMeUrl(): String {
        return String.format(
            COLOR_BLIND_SITE_ABOUT_ME,
            getDeviceLanguage()
        )
    }

    private fun getDeviceLanguage(): String {
        val language = Locale.getDefault().language
        if (!COLOR_API_SUPPORTED_LANGUAGES.contains(language)) {
            return COLOR_API_SUPPORTED_LANGUAGES[0]
        }
        return language
    }

    companion object {
        private val COLOR_API_SUPPORTED_LANGUAGES = arrayListOf(
            "en",
            "it",
            "fr",
            "de",
            "es"
        )
    }
}
