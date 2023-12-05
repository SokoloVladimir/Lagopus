package com.twendev.vulpes.lagopus.datasource

class ZerdaService(baseUrl: String? = null) {
    private val retrofit = RetrofitClient.getClient(baseUrl)
    val api: ZerdaApi = retrofit.create(ZerdaApi::class.java)

    fun get() {
        // TODO: написать обработчики на сырые методы api
    }
    companion object {
        var Singleton : ZerdaService = ZerdaService()
    }
}

