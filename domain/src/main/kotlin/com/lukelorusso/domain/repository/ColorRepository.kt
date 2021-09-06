package com.lukelorusso.domain.repository

import com.lukelorusso.domain.model.Color
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface ColorRepository {

    fun getPixelNeighbourhood(): Single<Int>

    fun setPixelNeighbourhood(count: Int): Completable

    fun getLastLensPosition(): Single<Int>

    fun setLastLensPosition(position: Int): Completable

    fun getLastZoomValue(): Single<Int>

    fun setLastZoomValue(position: Int): Completable

    fun getHelpUrl(): Single<String>

    fun getHomeUrl(): Single<String>

    fun getAboutMeUrl(): Single<String>

    fun getColor(colorHex: String, deviceUdid: String): Single<Color>

    fun getColorList(): Single<List<Color>>

    fun deleteColor(color: Color): Completable

    fun deleteAllColors(): Completable

}
