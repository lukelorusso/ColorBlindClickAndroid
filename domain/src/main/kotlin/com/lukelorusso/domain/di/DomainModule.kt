package com.lukelorusso.domain.di

import com.lukelorusso.domain.usecase.*
import org.koin.dsl.module

val domainModule = module {
    //region Repository
    factory { DeleteAllColorsUseCase(get()) }
    factory { DeleteColorUseCase(get()) }
    factory { GetAboutMeUrlUseCase(get()) }
    factory { GetColorUseCase(get()) }
    factory { GetColorListUseCase(get()) }
    factory { GetHelpUrlUseCase(get()) }
    factory { GetHomeUrlUseCase(get()) }
    factory { GetLastLensPositionUseCase(get()) }
    factory { SetLastLensPositionUseCase(get()) }
    factory { GetLastZoomValueUseCase(get()) }
    factory { SetLastZoomValueUseCase(get()) }
    //endregion
}
