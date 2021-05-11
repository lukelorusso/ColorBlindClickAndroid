package com.lukelorusso.presentation.di

import com.lukelorusso.domain.usecase.base.Logger
import com.lukelorusso.domain.usecase.base.UseCaseScheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.dsl.module

val testModule = module {
    //region AppComponent
    factory<Logger> {
        object : Logger {
            override fun log(message: () -> String) {
                println(message())
            }

            override fun logError(throwable: () -> Throwable) {
                println(throwable())
            }
        }
    }
    factory { UseCaseScheduler(Schedulers.io(), Schedulers.trampoline()) }
    //endregion
}
