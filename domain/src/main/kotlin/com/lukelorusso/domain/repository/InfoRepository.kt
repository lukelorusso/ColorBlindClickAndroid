package com.lukelorusso.domain.repository

interface InfoRepository {

    fun getHelpUrl(deviceLanguage: String): String

    fun getHomeUrl(deviceLanguage: String): String

    fun getAboutMeUrl(): String

    fun getStoreUrl(): String

}
