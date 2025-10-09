package com.lukelorusso.domain.repository

interface InfoRepository {

    fun getAboutAppUrl(): String

    fun getApiHomeUrl(deviceLanguage: String): String

    fun getApiHelpUrl(deviceLanguage: String): String

    fun getAboutMeUrl(): String

    fun getStoreUrl(): String

}
