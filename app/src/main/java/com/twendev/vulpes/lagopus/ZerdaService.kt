package com.twendev.vulpes.lagopus

import com.twendev.vulpes.lagopus.model.Work

class ZerdaService {
    private val retrofit = RetrofitClient.getClient()
    private val api = retrofit.create(ZerdaApi::class.java)

    suspend fun GetWorks(): Array<Work> {
        return api.getWorks()
    }
}

