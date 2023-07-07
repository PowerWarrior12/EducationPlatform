package com.example.educationplatform.data.remote.course

import com.example.educationplatform.data.remote.course.entities.*
import retrofit2.Response
import retrofit2.http.*

interface CourseApi {
    //region Course
    @POST("cource/create")
    suspend fun createCourse(@Body request: CreateCourseRequest): Response<Int>

    @PUT("cource/edit/{courseId}")
    suspend fun updateCourse(@Body request: EditCourseRequest, @Path("courseId") courseId: Int): Response<Void>

    @GET("cource/{courseId}")
    suspend fun getCourse(@Path("courseId") courseId: Int): Response<CourseResponse>

    @DELETE("/cource/{courseId}/delete")
    suspend fun deleteCourse(@Path("courseId") courseId: Int, @Query("id") courceId: Int): Response<Void>

    @GET("cource/subscribe/list")
    suspend fun getSubscriptionCourses(): Response<List<SubscriptionCoursesResponse.SubscriptionCourseResponse>>

    @GET("cource/my/list")
    suspend fun getUserCourses(): Response<List<UserCoursesResponse.UserCourseResponse>>

    @GET("cource/list")
    suspend fun getAllCourses(): Response<List<CourseResponse>>

    @POST("cource/{courseId}/subscribe")
    suspend fun subscribeToCourse(@Path("courseId") courseId: Int): Response<Void>

    @POST("cource/{courseId}/unsubscribe")
    suspend fun unsubscribeFromCourse(@Path("courseId") courseId: Int): Response<Void>

    @GET("cource/{courceId}/subscribe/check")
    suspend fun checkUserSubscription(@Path("courceId") courceId: Int, @Query("courceId") courseId: Int): Response<Boolean>

    //endregion

    //region Module
    @PUT("/cource/module/{moduleId}/edit")
    suspend fun updateModule(@Body request: EditModuleRequest, @Path("moduleId") moduleId: Int): Response<Void>

    @POST("cource/{courseId}/module/create")
    suspend fun createModule(@Body request: CreateModuleRequest, @Path("courseId") courseId: Int): Response<Int>

    @GET("cource/{courseId}/module/list")
    suspend fun getModules(@Path("courseId")courseId: Int): Response<List<ModuleResponse>>

    @DELETE("cource/module/{moduleId}/delete")
    suspend fun deleteModule(@Path("moduleId") moduleId: Int): Response<Void>

    @GET("cource/{courseId}/module/list/subscribe")
    suspend fun getTakesModules(@Path("courseId") courseId: Int): Response<List<TakesModuleResponse>>

    //endregion

    //region Stage
    @POST("module/{moduleId}/stage/create")
    suspend fun createStage(@Body request: CreateStageRequest, @Path("moduleId") moduleId: Int): Response<Int>

    @PUT("module/stage/{stageId}/edit")
    suspend fun updateStage(@Body request: EditStageRequest, @Path("stageId") stageId: Int): Response<Void>

    @POST("stage/result")
    suspend fun updateStageResult(@Body request: UpdateStageResultRequest): Response<Void>

    @DELETE("module/stage/{stageId}/delete")
    suspend fun deleteStage(@Path("stageId") stageId: Int): Response<Void>

    //endregion
}