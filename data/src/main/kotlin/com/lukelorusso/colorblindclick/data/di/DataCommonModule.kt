package com.lukelorusso.colorblindclick.data.di

import com.lukelorusso.colorblindclick.data.extensions.api
import com.lukelorusso.colorblindclick.data.mapper.SaveDevMapper
import com.lukelorusso.colorblindclick.data.mapper.TheColorMapper
import com.lukelorusso.colorblindclick.data.repository.*
import com.lukelorusso.domain.repository.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import retrofit2.Retrofit

val dataCommonModule = module {
    //region Mapper
    factory<Json> { Json { ignoreUnknownKeys = true } }
    factory { SaveDevMapper() }
    factory { TheColorMapper() }
    //endregion

    //region Repository
    factory<SaveDevApiRepository> {
        SaveDevApiRepositoryImpl(
            (get() as Retrofit).api(),
            get(),
            get(),
            get()
        )
    }
    factory<TheColorApiRepository> {
        TheColorApiRepositoryImpl(
            (get() as Retrofit).api(),
            get(),
            get(),
            get()
        )
    }
    factory<InfoRepository> { InfoRepositoryImpl() }
    factory<HistoryRepository> {
        HistoryRepositoryImpl(
            get()
        )
    }
    factory<SettingsRepository> {
        SettingsRepositoryImpl(
            get()
        )
    }
    //endregion
}
