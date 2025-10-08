package com.lukelorusso.data.repository

import com.lukelorusso.data.net.SaveDevRetrofitFactory.COLOR_BLIND_SITE_ABOUT_ME
import com.lukelorusso.data.net.SaveDevRetrofitFactory.COLOR_BLIND_SITE_HELP
import com.lukelorusso.data.net.SaveDevRetrofitFactory.COLOR_BLIND_SITE_HOME
import com.lukelorusso.domain.repository.InfoRepository
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
        if (!APP_SUPPORTED_LANGUAGES.contains(language)) {
            return APP_SUPPORTED_LANGUAGES[0]
        }
        return language
    }
}
