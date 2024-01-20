package com.twendev.vulpes.lagopus.datasource

import com.twendev.vulpes.lagopus.util.UtilLogger
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor(private val logger: UtilLogger) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        logger.debug("Retrofit", request.toString())
        return chain.proceed(request)
    }
}