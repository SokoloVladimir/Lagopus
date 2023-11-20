package com.twendev.vulpes.lagopus

import com.twendev.vulpes.lagopus.model.*
import retrofit2.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ZerdaApi {
    @GET("/Work")
    suspend fun getWorks() : Array<Work>
    @GET("/WorkType")
    suspend fun getWorkTypes() : Array<WorkType>
    @GET("/Discipline")
    suspend fun getDisciplines() : Array<Discipline>
    @PUT("/Discipline")
    suspend fun putDiscipline(@Body discipline: Discipline)
    @GET("/Student")
    suspend fun getStudents() : Array<Student>
    @GET("/Group")
    suspend fun getGroups() : Array<Group>
    @GET("/Account")
    suspend fun getAccounts() : Array<Account>
}