package com.lukelorusso.data.di.modules

import com.lukelorusso.data.di.providers.PersistenceProvider
import com.lukelorusso.data.persistence.AppDatabase
import com.lukelorusso.data.persistence.DatabaseFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Copyright (C) 2020 Mikhael LOPEZ
 * Licensed under the Apache License Version 2.0
 * Dagger module that provides Persistence class.
 */
@Module
class PersistenceModule {

    @Provides
    @Singleton
    internal fun provideAppDatabase() = DatabaseFactory.getDatabase()

    @Provides
    @Singleton
    internal fun provideDatabase(appDatabase: AppDatabase) = PersistenceProvider(appDatabase)

}
