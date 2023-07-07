package com.example.educationplatform.data.remote.report

import com.example.educationplatform.data.remote.report.entities.CreateReportRequest
import com.example.educationplatform.data.remote.report.entities.ReportResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReportApi {
    @POST("cource/{courceId}/report/create")
    suspend fun createReport(@Body createReportRequest: CreateReportRequest, @Path("courceId") courseId: Int): Response<Unit>

    @GET("cource/{courceId}/report/list")
    suspend fun getCourseReports(@Path("courceId") courseId: Int): Response<List<ReportResponse>>
}