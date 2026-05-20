package com.lukelorusso.colorblindclick.domain.repository

interface InfoRepository {

    fun getAboutAppUrl(): String

    fun getAboutMeUrl(): String

    fun getStoreUrl(): String

}
