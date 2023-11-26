package com.twendev.vulpes.lagopus.datasource

import com.twendev.vulpes.lagopus.model.*
import retrofit2.*
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ZerdaApi {
    @GET("/Work")
    suspend fun getWorks(
        @Query("disciplineId")  disciplineId : Int? = null,
        @Query("workTypeId")    workTypeId : Int? = null,
        @Query("semesterId")    semesterId : Int? = null,
    ) : Array<Work>
    @POST("/Work")
    suspend fun postWork(@Body item: Work) : Work
    @PUT("/Work")
    suspend fun putWork(@Body item: Work)
    @DELETE("/Work/{id}")
    suspend fun deleteWork(@Path("id") id: Int)

    @GET("/WorkType")
    suspend fun getWorkTypes() : Array<WorkType>
    @POST("/WorkType")
    suspend fun postWorkType(@Body item: WorkType) : WorkType
    @PUT("/WorkType")
    suspend fun putWorkType(@Body item: WorkType)
    @DELETE("/WorkType/{id}")
    suspend fun deleteWorkType(@Path("id") id: Int)

    @GET("/Discipline")
    suspend fun getDisciplines() : Array<Discipline>
    @POST("/Discipline")
    suspend fun postDiscipline(@Body item: Discipline) : Discipline
    @PUT("/Discipline")
    suspend fun putDiscipline(@Body item: Discipline)
    @DELETE("/Discipline/{id}")
    suspend fun deleteDiscipline(@Path("id") id: Int)

    @GET("/Semester")
    suspend fun getSemesters() : Array<Semester>
    @POST("/Semester")
    suspend fun postSemester(@Body item: Semester) : Semester
    @PUT("/Semester")
    suspend fun putSemester(@Body item: Semester)
    @DELETE("/Semester/{id}")
    suspend fun deleteSemester(@Path("id") id: Int)

    @GET("/Student")
    suspend fun getStudents() : Array<Student>
    @GET("/Group")
    suspend fun getGroups() : Array<Group>
    @GET("/Account")
    suspend fun getAccounts() : Array<Account>
}