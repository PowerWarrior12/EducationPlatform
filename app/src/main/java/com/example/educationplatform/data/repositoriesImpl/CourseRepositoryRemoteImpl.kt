package com.example.educationplatform.data.repositoriesImpl

import com.example.educationplatform.data.mappers.*
import com.example.educationplatform.data.remote.course.CourseApi
import com.example.educationplatform.data.remote.direction.DirectionApi
import com.example.educationplatform.domain.entities.*
import com.example.educationplatform.domain.repositories.CoursesRepositoryRemote
import javax.inject.Inject

class CourseRepositoryRemoteImpl @Inject constructor(
    private val courseApi: CourseApi,
    private val directionApi: DirectionApi
) : CoursesRepositoryRemote {
    override suspend fun getTakesCourses(): Result<List<SubCourse>> {
        val response = courseApi.getSubscriptionCourses()
        return if (response.isSuccessful) {
            Result.success(response.body()!!.map { subscriptionCourseResponse ->
                subscriptionCourseResponse.toSubCourse()
            })
        } else {
            Result.failure(java.lang.Exception(response.message()))
        }
    }

    override suspend fun getEditCourses(): Result<List<UsersCourse>> {
        val response = courseApi.getUserCourses()
        return if (response.isSuccessful) {
            Result.success(response.body()!!.map { userCourseResponse ->
                userCourseResponse.toUsersCourse()
            })
        } else {
            Result.failure(java.lang.Exception(response.message()))
        }
    }

    override suspend fun getCourses(): Result<List<Course>> {
        val response = courseApi.getAllCourses()
        return if (response.isSuccessful) {
            Result.success(response.body()!!.map { courseResponse ->
                courseResponse.toCourse()
            })
        } else {
            Result.failure(java.lang.Exception(response.message()))
        }
    }

    override suspend fun isUserInCourse(courseId: Int): Result<Boolean> {
        val response = courseApi.checkUserSubscription(courseId, courseId)
        return if (response.isSuccessful) {
            Result.success(response.body()!!)
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    override suspend fun getFullCourseInfo(courseId: Int): Result<Course> {
        val response = courseApi.getCourse(courseId)
        return if (response.isSuccessful) {
            Result.success(response.body()!!.toCourse())
        } else {
            Result.failure(java.lang.Exception(response.message()))
        }
    }

    override suspend fun getDirections(): Result<List<String>> {
        val response = directionApi.getDirections()
        return if (response.isSuccessful) {
            Result.success(response.body()!!)
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    override suspend fun createCourse(course: Course): Result<Int> {
        val response = courseApi.createCourse(course.toCreateCourseRequest())
        return if (response.isSuccessful) {
            Result.success(response.body()!!)
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    override suspend fun updateCourse(course: Course): Result<Unit> {
        val response = courseApi.updateCourse(course.toEditCourseRequest(), course.id)
        return if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    override suspend fun deleteCourse(courseId: Int): Result<Unit> {
        val response = courseApi.deleteCourse(courseId, courseId)
        return if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    override suspend fun createModule(module: Module): Result<Int> {
        val response = courseApi.createModule(module.toCreateModuleRequest(), module.courseId)
        return if (response.isSuccessful) {
            Result.success(response.body()!!)
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    override suspend fun createStage(stage: Stage): Result<Int> {
        val response = courseApi.createStage(stage.toCreateStageRequest(), stage.moduleId)
        return if (response.isSuccessful) {
            Result.success(response.body()!!)
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    override suspend fun getModulesByCourseId(courseId: Int): Result<List<Module>> {
        val response = courseApi.getModules(courseId)
        return if (response.isSuccessful) {
            Result.success(response.body()!!.map { moduleResponse ->
                moduleResponse.toModule()
            })
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    override suspend fun updateModule(module: Module): Result<Unit> {
        val response = courseApi.updateModule(module.toEditModuleRequest(), module.id)
        return if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    override suspend fun updateStage(stage: Stage): Result<Unit> {
        val response = courseApi.updateStage(stage.toEditStageRequest(), stage.id)
        return if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    override suspend fun deleteModule(moduleId: Int): Result<Unit> {
        val response = courseApi.deleteModule(moduleId)
        return if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    override suspend fun deleteStage(stageId: Int): Result<Unit> {
        val response = courseApi.deleteStage(stageId)
        return if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    override suspend fun getTakesModules(courseId: Int): Result<List<TakesModule>> {
        val response = courseApi.getTakesModules(courseId)
        return if (response.isSuccessful) {
            Result.success(response.body()!!.map { takesModuleResponse ->
                takesModuleResponse.toTakesModule()
            })
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    override suspend fun updateTakesStage(takesStage: TakesStage): Result<Unit> {
        val response = courseApi.updateStageResult(takesStage.toUpdateStageResultRequest())
        return if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    override suspend fun subscribe(courseId: Int): Result<Unit> {
        val response = courseApi.subscribeToCourse(courseId)
        return if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    override suspend fun unsubscribe(courseId: Int): Result<Unit> {
        val response = courseApi.unsubscribeFromCourse(courseId)
        return if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.message()))
        }
    }
}