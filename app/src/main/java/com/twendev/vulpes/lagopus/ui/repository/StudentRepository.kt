package com.twendev.vulpes.lagopus.ui.repository

import com.twendev.vulpes.lagopus.datasource.ZerdaService
import com.twendev.vulpes.lagopus.model.Student

class StudentRepository : RepositoryInterface<Student> {
    private val zerdaSource = ZerdaService.Singleton

    suspend fun get(id: Int) : Student? {
        return zerdaSource.api.getStudents().firstOrNull { it.id == id}
    }
    override suspend fun get(): List<Student> {
        return zerdaSource.api.getStudents().toList()
    }
    suspend fun getByGroupId(groupId: Int) : List<Student> {
        return zerdaSource.api.getStudents(groupId).toList()
    }
    override suspend fun update(obj: Student) {
        return zerdaSource.api.putStudent(obj)
    }
    override suspend fun create(obj: Student) : Student {
        return zerdaSource.api.postStudent(obj)
    }
    override suspend fun delete(obj: Student) {
        zerdaSource.api.deleteStudent(obj.id)
    }
}