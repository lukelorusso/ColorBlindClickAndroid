package com.lukelorusso.data.repository

import com.lukelorusso.data.di.providers.PersistenceManager
import com.lukelorusso.data.di.providers.SessionManager
import com.lukelorusso.data.extensions.catchPersistenceException
import com.lukelorusso.data.mapper.ColorMapper
import com.lukelorusso.data.net.HttpServiceManager
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
    private val httpServiceManager: HttpServiceManager,
    private val sessionManager: SessionManager,
    private val persistenceManager: PersistenceManager
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

    override fun getPixelNeighbourhood(): Single<Int> =
        Single.just(sessionManager.getPixelNeighbourhood())

    override fun setPixelNeighbourhood(count: Int): Completable =
        Single.just(sessionManager.setPixelNeighbourhood(count))
            .ignoreElement()

    override fun getLastLensPosition(): Single<Int> =
        Single.just(sessionManager.getLastLensPosition())

    override fun setLastLensPosition(position: Int): Completable =
        Single.just(sessionManager.setLastLensPosition(position))
            .doAfterSuccess { sessionManager.deleteLastZoomValue() }
            .ignoreElement()

    override fun getLastZoomValue(): Single<Int> =
        Single.just(sessionManager.getLastZoomValue())

    override fun setLastZoomValue(position: Int): Completable =
        Single.just(sessionManager.setLastZoomValue(position))
            .ignoreElement()

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

    override fun getColor(colorHex: String, deviceUdid: String): Single<Color> =
        httpServiceManager.execute(
            call = api.getColor(
                colorHex.let {
                    if (it.contains("#"))
                        it.replace("#", "")
                    else
                        it
                },
                getDeviceLanguage(),
                deviceUdid
            ),
            mapper = { colorMapper.transform(it) }
        ).flatMap { model ->
            Single.fromCallable {
                persistenceManager.getColorList().toMutableList().apply {
                    val existent = find { oldModel -> oldModel.colorHex == model.colorHex }
                    existent?.also { remove(it) }
                    add(0, model)
                    persistenceManager.saveColorList(this)
                }
            }.map { model }
        }.onErrorResumeNext { e -> e.catchPersistenceException() }

    override fun getColorList(): Single<List<Color>> = Single
        .fromCallable { persistenceManager.getColorList() }
        .onErrorResumeNext { e -> e.catchPersistenceException() }

    override fun deleteColor(color: Color): Completable = Single
        .fromCallable {
            persistenceManager.getColorList().toMutableList().apply {
                val existent = find { c -> c.colorHex == color.colorHex }
                existent?.also { remove(it) }
                persistenceManager.saveColorList(this)
            }
        }
        .onErrorResumeNext { e -> e.catchPersistenceException() }
        .ignoreElement()

    override fun deleteAllColors(): Completable = Single
        .fromCallable { persistenceManager.clearColorList() }
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
