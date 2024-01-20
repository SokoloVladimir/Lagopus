package com.twendev.vulpes.lagopus.ui.repository

import com.twendev.vulpes.lagopus.datasource.AuthOptions
import com.twendev.vulpes.lagopus.datasource.ZerdaService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthRepository : KoinComponent {
    private val zerdaSource : ZerdaService by inject()
    private val authOptions : AuthOptions by inject()
    val authRole : String?
        get() = authOptions.bearer?.role


    suspend fun auth(login: String, password: String) : AuthRepository {
        val result = zerdaSource.api.getBearer(login, password)
        authOptions.bearer = result
        return this
    }

    fun setInstanceUrl(url: String) : AuthRepository {
        authOptions.instanceUrl = url
        return this
    }
}