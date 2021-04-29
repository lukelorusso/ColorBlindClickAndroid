package com.lukelorusso.data.repository

import com.google.gson.Gson
import com.lukelorusso.data.extensions.api
import com.lukelorusso.data.mapper.ColorMapper
import com.lukelorusso.data.net.OkHttpClientFactory
import com.lukelorusso.data.net.RetrofitFactory

/**
 * @author LukeLorusso on 25-03-2021.
 */
class TestColorDataRepository {

    fun getRepository(): ColorDataRepository = OkHttpClientFactory()
        .let { okHttpClientFactory ->
            Gson().let { gson ->
                RetrofitFactory.getRetrofitTestBuilder(
                    gson,
                    okHttpClientFactory
                ).let { retrofit ->
                    ColorDataRepository(retrofit.api(), ColorMapper())
                }
            }
        }

}
