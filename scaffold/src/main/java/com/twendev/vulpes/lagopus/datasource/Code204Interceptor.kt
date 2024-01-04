package com.twendev.vulpes.lagopus.datasource

import okhttp3.Interceptor
import okhttp3.Response

class Code204Interceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        return if (response.code() == 204) {
            response.newBuilder().code(200).build()
        } else {
            response
        }
    }
}