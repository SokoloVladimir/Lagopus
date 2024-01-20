package com.twendev.vulpes.lagopus.datasource

import com.twendev.vulpes.lagopus.util.UtilLogger

class ZerdaService(authOptions: AuthOptions, utilLogger: UtilLogger) {
    private val retrofit = RetrofitClient(authOptions, utilLogger).getClient(authOptions.instanceUrl)
    val api: ZerdaApi = retrofit.create(ZerdaApi::class.java)

    fun get() {
        // TODO: написать обработчики на сырые методы api
    }
}

