package com.lukelorusso.data.di.modules

import com.lukelorusso.data.di.providers.NetworkChecker
import com.lukelorusso.data.di.providers.PersistenceProvider
import com.lukelorusso.data.di.providers.SessionManager
import com.lukelorusso.data.extensions.api
import com.lukelorusso.data.mapper.ColorMapper
import com.lukelorusso.data.repository.ColorDataRepository
import com.lukelorusso.domain.repository.ColorRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Copyright (C) 2020 Mikhael LOPEZ
 * Licensed under the Apache License Version 2.0
 * Dagger module that provides Repository class.
 */
@Module
class RepositoryModule {

    @Provides
    @Singleton
    internal fun provideColorRepository(
        retrofit: Retrofit,
        mapper: ColorMapper,
        networkChecker: NetworkChecker,
        sessionManager: SessionManager,
        persistenceProvider: PersistenceProvider
    ): ColorRepository =
        ColorDataRepository(
            retrofit.api(),
            mapper,
            networkChecker,
            sessionManager,
            persistenceProvider
        )

}
