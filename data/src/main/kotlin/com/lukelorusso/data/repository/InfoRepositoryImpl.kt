package com.lukelorusso.data.repository

import com.lukelorusso.data.net.SaveDevRetrofitFactory
import com.lukelorusso.data.net.TheColorRetrofitFactory
import com.lukelorusso.domain.repository.InfoRepository

class InfoRepositoryImpl : InfoRepository {
    override fun getHelpUrl(deviceLanguage: String): String =
        if (deviceLanguage == "en") TheColorRetrofitFactory.WEBSITE_HELP
        else String.format(
            SaveDevRetrofitFactory.WEBSITE_HELP,
            deviceLanguage
        )

    override fun getHomeUrl(deviceLanguage: String): String =
        if (deviceLanguage == "en") TheColorRetrofitFactory.WEBSITE
        else String.format(
            SaveDevRetrofitFactory.WEBSITE_HOME,
            deviceLanguage
        )

    override fun getAboutMeUrl(): String =
        WEBSITE_ABOUT_ME
}
