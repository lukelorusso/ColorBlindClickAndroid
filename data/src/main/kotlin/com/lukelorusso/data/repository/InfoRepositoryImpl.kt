package com.lukelorusso.data.repository

import com.lukelorusso.data.net.SaveDevRetrofitFactory
import com.lukelorusso.data.net.TheColorRetrofitFactory
import com.lukelorusso.domain.repository.InfoRepository

class InfoRepositoryImpl : InfoRepository {
    override fun getApiHomeUrl(deviceLanguage: String): String =
        if (deviceLanguage == "en") TheColorRetrofitFactory.WEBSITE
        else String.format(
            SaveDevRetrofitFactory.WEBSITE_HOME,
            deviceLanguage
        )

    override fun getApiHelpUrl(deviceLanguage: String): String =
        if (deviceLanguage == "en") TheColorRetrofitFactory.WEBSITE_HELP
        else String.format(
            SaveDevRetrofitFactory.WEBSITE_HELP,
            deviceLanguage
        )

    override fun getAboutAppUrl(): String =
        WEBSITE_ABOUT_APP

    override fun getAboutMeUrl(): String =
        WEBSITE_ABOUT_ME

    override fun getStoreUrl(): String =
        GOOGLE_PLAY_STORE_URL
}
