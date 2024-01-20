package com.twendev.vulpes.lagopus

import android.content.Context
import android.util.Log
import com.twendev.vulpes.lagopus.datasource.AuthOptions
import com.twendev.vulpes.lagopus.datasource.ZerdaService
import com.twendev.vulpes.lagopus.ui.repository.AssignmentRepository
import com.twendev.vulpes.lagopus.ui.repository.AuthRepository
import com.twendev.vulpes.lagopus.ui.repository.DisciplineRepository
import com.twendev.vulpes.lagopus.ui.repository.ResultRepository
import com.twendev.vulpes.lagopus.ui.repository.SemesterRepository
import com.twendev.vulpes.lagopus.ui.repository.StudentRepository
import com.twendev.vulpes.lagopus.ui.repository.WorkRepository
import com.twendev.vulpes.lagopus.ui.repository.WorkTypeRepository
import com.twendev.vulpes.lagopus.util.UtilLogger
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.logger.Level
import org.koin.dsl.module

fun startKoinWithAndroidContext(ctx: Context) {
    val appModule = module {
        single { AuthOptions() }
        single { UtilLogger { tag, msg ->
            Log.d(tag, msg)
        }}

        factory { ZerdaService(get(), get()) }

        factory { AssignmentRepository() }
        factory { AuthRepository() }
        factory { DisciplineRepository() }
        factory { ResultRepository() }
        factory { SemesterRepository() }
        factory { StudentRepository() }
        factory { WorkRepository() }
        factory { WorkTypeRepository() }
    }

    GlobalContext.startKoin {
        androidLogger(Level.INFO)
        androidContext(ctx)

        modules(appModule)
    }
}