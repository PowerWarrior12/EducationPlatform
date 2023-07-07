package com.example.educationplatform.domain.repositories

import com.example.educationplatform.domain.entities.*

interface CoursesRepositoryRemote {
    suspend fun getTakesCourses(): Result<List<SubCourse>>
    suspend fun getEditCourses(): Result<List<UsersCourse>>
    suspend fun getCourses(): Result<List<Course>>
    suspend fun isUserInCourse(courseId: Int): Result<Boolean>
    suspend fun getFullCourseInfo(courseId: Int): Result<Course>
    suspend fun getDirections(): Result<List<String>>
    suspend fun createCourse(course: Course): Result<Int>
    suspend fun updateCourse(course: Course): Result<Unit>
    suspend fun deleteCourse(courseId: Int): Result<Unit>
    suspend fun createModule(module: Module): Result<Int>
    suspend fun createStage(stage: Stage): Result<Int>
    suspend fun getModulesByCourseId(courseId: Int): Result<List<Module>>
    suspend fun updateModule(module: Module): Result<Unit>
    suspend fun updateStage(stage: Stage): Result<Unit>
    suspend fun deleteModule(moduleId: Int): Result<Unit>
    suspend fun deleteStage(stageId: Int): Result<Unit>
    suspend fun getTakesModules(courseId: Int): Result<List<TakesModule>>
    suspend fun updateTakesStage(takesStage: TakesStage): Result<Unit>
    suspend fun subscribe(courseId: Int): Result<Unit>
    suspend fun unsubscribe(courseId: Int): Result<Unit>
}