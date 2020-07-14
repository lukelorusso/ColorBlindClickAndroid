package com.lukelorusso.data.repository

import com.lukelorusso.data.di.providers.NetworkChecker
import com.lukelorusso.data.di.providers.PersistenceProvider
import com.lukelorusso.data.di.providers.SessionManager
import com.lukelorusso.data.extensions.catchPersistenceException
import com.lukelorusso.data.extensions.catchWebServiceException
import com.lukelorusso.data.mapper.ColorMapper
import com.lukelorusso.data.net.RetrofitFactory.COLOR_BLIND_SITE_ABOUT_ME
import com.lukelorusso.data.net.RetrofitFactory.COLOR_BLIND_SITE_HELP
import com.lukelorusso.data.net.RetrofitFactory.COLOR_BLIND_SITE_HOME
import com.lukelorusso.data.net.api.ColorApi
import com.lukelorusso.domain.model.Color
import com.lukelorusso.domain.repository.ColorRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.util.*

class ColorDataRepository(
    private val api: ColorApi,
    private val colorMapper: ColorMapper,
    private val networkChecker: NetworkChecker,
    private val sessionManager: SessionManager,
    private val persistenceProvider: PersistenceProvider
) : ColorRepository {

    companion object {
        private val COLOR_API_SUPPORTED_LANGUAGES = arrayListOf(
            "en",
            "it",
            "fr",
            "de",
            "es"
        )
    }

    override val isConnected: Boolean
        get() = networkChecker.isConnected

    override fun getLastLensPosition(): Single<Int> =
        Single.just(sessionManager.getLastLensPosition())

    override fun setLastLensPosition(position: Int): Completable =
        Single.just(sessionManager.setLastLensPosition(position)).ignoreElement()

    override fun getHelpUrl(): Single<String> = Single.just(
        String.format(
            COLOR_BLIND_SITE_HELP,
            getDeviceLanguage()
        )
    )

    override fun getHomeUrl(): Single<String> = Single.just(
        String.format(
            COLOR_BLIND_SITE_HOME,
            getDeviceLanguage()
        )
    )

    override fun getAboutMeUrl(): Single<String> = Single.just(
        String.format(
            COLOR_BLIND_SITE_ABOUT_ME,
            getDeviceLanguage()
        )
    )

    override fun getColor(colorHex: String, deviceUdid: String): Single<Color> {
        return api.getColor(
            colorHex.let { if (it.contains("#")) it.replace("#", "") else it },
            getDeviceLanguage(),
            deviceUdid
        )
            .map { colorMapper.transform(it) }
            .doOnSuccess { persistenceProvider.saveColor(it) }
            .onErrorResumeNext { e -> e.catchWebServiceException() }
            .onErrorResumeNext { e -> e.catchPersistenceException() }
    }

    override fun getColorList(): Single<List<Color>> = Single
        .fromCallable { persistenceProvider.getColorList() }
        .onErrorResumeNext { e -> e.catchPersistenceException() }

    override fun deleteColor(color: Color): Completable = Single
        .fromCallable { persistenceProvider.deleteColor(color) }
        .onErrorResumeNext { e -> e.catchPersistenceException() }
        .ignoreElement()

    override fun deleteAllColors(): Completable = Single
        .fromCallable { persistenceProvider.deleteAllColors() }
        .onErrorResumeNext { e -> e.catchPersistenceException() }
        .ignoreElement()

    private fun getDeviceLanguage(): String {
        val language = Locale.getDefault().language
        if (!COLOR_API_SUPPORTED_LANGUAGES.contains(language)) {
            return COLOR_API_SUPPORTED_LANGUAGES[0]
        }
        return language
    }

}
