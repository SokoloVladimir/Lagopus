package com.twendev.vulpes.lagopus.ui.repository

interface RepositoryInterface<T> {
    suspend fun pullAndGet(): List<T>
    suspend fun updateAndPush(obj: T)
    suspend fun deleteAndPush(obj: T)
    suspend fun createAndPush(obj: T) : T
}