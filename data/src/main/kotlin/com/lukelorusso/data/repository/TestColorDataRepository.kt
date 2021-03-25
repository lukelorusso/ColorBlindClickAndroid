package com.lukelorusso.data.repository

import android.content.Context
import com.google.gson.Gson
import com.lukelorusso.data.di.providers.NetworkChecker
import com.lukelorusso.data.di.providers.PersistenceProvider
import com.lukelorusso.data.di.providers.SessionManager
import com.lukelorusso.data.di.providers.SharedPrefProvider
import com.lukelorusso.data.extensions.api
import com.lukelorusso.data.mapper.ColorMapper
import com.lukelorusso.data.net.OkHttpClientFactory
import com.lukelorusso.data.net.RetrofitFactory
import com.lukelorusso.data.persistence.AppDatabase

/**
 * @author LukeLorusso on 25-03-2021.
 */
class TestColorDataRepository {

    fun getRepository(context: Context): ColorDataRepository = OkHttpClientFactory()
            .let { okHttpClientFactory ->
                Gson().let { gson ->
                    RetrofitFactory.getLocalRetrofit(
                            context,
                            gson,
                            okHttpClientFactory
                    ).let { retrofit ->
                        ColorDataRepository(
                                retrofit.api(),
                                ColorMapper(),
                                NetworkChecker(context),
                                SessionManager(SharedPrefProvider(context)),
                                PersistenceProvider(AppDatabase())
                        )
                    }
                }
            }

}
