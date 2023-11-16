package com.twendev.vulpes.lagopus

import com.twendev.vulpes.lagopus.model.Work
import retrofit2.*
import retrofit2.http.GET

interface ZerdaApi {
    @GET("/Work")
    suspend fun getWorks() : Array<Work>
}