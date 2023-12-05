package com.twendev.vulpes.lagopus.ui.repository

interface RepositoryInterface<T> {
    suspend fun get(): List<T>
    suspend fun update(obj: T)
    suspend fun delete(obj: T)
    suspend fun create(obj: T) : T
}