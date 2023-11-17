package com.twendev.vulpes.lagopus

import com.twendev.vulpes.lagopus.model.Work

class ZerdaService(baseUrl: String? = null) {
    private val retrofit = RetrofitClient.getClient(baseUrl)
    private val api = retrofit.create(ZerdaApi::class.java)

    suspend fun GetWorks(): Array<Work> {
        return api.getWorks()
    }
}

