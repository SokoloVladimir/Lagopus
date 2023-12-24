package com.twendev.vulpes.lagopus.datasource

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

object RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.d("RetrofitClient","${request.method()} ${request.url()} ${request.headers()}")
        return chain.proceed(request)
    }
}