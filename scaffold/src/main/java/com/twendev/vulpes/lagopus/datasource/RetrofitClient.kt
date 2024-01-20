package com.twendev.vulpes.lagopus.datasource

import com.twendev.vulpes.lagopus.util.UtilLogger
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(
    private val authOptions: AuthOptions,
    private val utilLogger: UtilLogger
) {
    private val okHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(RequestInterceptor(utilLogger))
        .addInterceptor(BearerInterceptor(authOptions))
        .build()

    fun getClient(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

