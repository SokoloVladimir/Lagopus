package com.twendev.vulpes.lagopus

import com.twendev.vulpes.lagopus.model.*
import retrofit2.*
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ZerdaApi {
    @GET("/Work")
    suspend fun getWorks() : Array<Work>
    @GET("/WorkType")
    suspend fun getWorkTypes() : Array<WorkType>
    @GET("/Discipline")
    suspend fun getDisciplines() : Array<Discipline>
    @POST("/Discipline")
    suspend fun postDiscipline(@Body discipline: Discipline) : Discipline
    @PUT("/Discipline")
    suspend fun putDiscipline(@Body discipline: Discipline)
    @DELETE("/Discipline/{id}")
    suspend fun deleteDiscipline(@Path("id") id: Int)
    @GET("/Student")
    suspend fun getStudents() : Array<Student>
    @GET("/Group")
    suspend fun getGroups() : Array<Group>
    @GET("/Account")
    suspend fun getAccounts() : Array<Account>
}