package com.lukelorusso.data.repository

import com.lukelorusso.domain.repository.InfoRepository

class InfoRepositoryImpl : InfoRepository {
    override fun getAboutAppUrl(): String =
        WEBSITE_ABOUT_APP

    override fun getAboutMeUrl(): String =
        WEBSITE_ABOUT_ME

    override fun getStoreUrl(): String =
        GOOGLE_PLAY_STORE_URL
}
