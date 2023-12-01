package com.twendev.vulpes.lagopus.ui.repository

sealed class Repositories {
    companion object {
        val discipline : DisciplineRepository = DisciplineRepository()
        val workType : WorkTypeRepository = WorkTypeRepository()
        val work : WorkRepository = WorkRepository()
        val semester : SemesterRepository = SemesterRepository()
        val group : GroupRepository = GroupRepository()
        val assignment : AssignmentRepository = AssignmentRepository()
    }
}