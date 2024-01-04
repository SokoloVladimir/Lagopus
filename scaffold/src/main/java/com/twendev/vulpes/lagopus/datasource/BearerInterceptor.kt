package com.twendev.vulpes.lagopus.datasource

import okhttp3.Interceptor
import okhttp3.Response

object BearerInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (ZerdaService.Singleton.bearer != null) {
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${ZerdaService.Singleton.bearer!!.accessToken}")
                .build()
            chain.proceed(request)
        } else {
            chain.proceed(chain.request())
        }
    }
}