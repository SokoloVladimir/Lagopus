package com.twendev.vulpes.lagopus

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.twendev.vulpes.lagopus.datasource.ZerdaService
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ApiInstrumentedTest {
    fun ensureZerdaService() {
        ZerdaService.Singleton = ZerdaService()

        runBlocking {
            ZerdaService.Singleton.api.healthCheck()
        }
    }

    private fun authorizeWith(login: String, password: String) {
        ZerdaService.Singleton.apply {
            runBlocking {
                bearer = api.getBearer(login, password)
            }
        }
    }

    @Test
    fun authorizeTeacher_Test() {
        ensureZerdaService()
        authorizeWith("admin", "admin")

        assert(ZerdaService.Singleton.bearer?.role == "teacher")
    }

    @Test
    fun authorizeStudent_Test() {
        ensureZerdaService()
        authorizeWith("falcon", "falcon")

        assert(ZerdaService.Singleton.bearer?.role == "student")
    }

    @Test
    fun authorizedGetWorks_Test() {
        ensureZerdaService()
        authorizeWith("admin", "admin")

        runBlocking {
            assert(!ZerdaService.Singleton.api.getWorks().contentEquals(arrayOf()));
        }
    }
}