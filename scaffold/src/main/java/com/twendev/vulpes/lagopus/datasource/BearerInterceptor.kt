package com.twendev.vulpes.lagopus.datasource

import okhttp3.Interceptor
import okhttp3.Response

class BearerInterceptor(private val authOptions: AuthOptions) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (authOptions.bearer != null) {
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${authOptions.bearer?.accessToken}")
                .build()
            chain.proceed(request)
        } else {
            chain.proceed(chain.request())
        }
    }
}