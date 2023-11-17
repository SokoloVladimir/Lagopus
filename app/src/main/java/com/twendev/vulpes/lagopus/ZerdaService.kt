package com.twendev.vulpes.lagopus

class ZerdaService(baseUrl: String? = null) {
    private val retrofit = RetrofitClient.getClient(baseUrl)
    val api: ZerdaApi = retrofit.create(ZerdaApi::class.java)

    companion object {
        var Singleton : ZerdaService? = ZerdaService()
    }
}

