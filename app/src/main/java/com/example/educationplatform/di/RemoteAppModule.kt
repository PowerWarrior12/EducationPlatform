package com.example.educationplatform.di

import com.example.educationplatform.compilerApi
import com.example.educationplatform.data.remote.RetrofitService
import com.example.educationplatform.data.remote.chat.ChatApi
import com.example.educationplatform.data.remote.codeExecution.CodeExecutionApi
import com.example.educationplatform.data.remote.course.CourseApi
import com.example.educationplatform.data.remote.direction.DirectionApi
import com.example.educationplatform.data.remote.report.ReportApi
import com.example.educationplatform.data.remote.user.UserApi
import com.example.educationplatform.educationAppApi
import dagger.Module
import dagger.Provides

@Module
class RemoteAppModule {
    @Provides
    fun provideCodeExecutionApi(): CodeExecutionApi {
        return RetrofitService()
            .getRetrofit(compilerApi)
            .create(CodeExecutionApi::class.java)
    }

    @Provides
    fun provideUserApi(): UserApi {
        return RetrofitService()
            .getRetrofit(educationAppApi, true)
            .create(UserApi::class.java)
    }

    @Provides
    fun provideCourseApi(): CourseApi {
        return RetrofitService()
            .getRetrofit(educationAppApi, true)
            .create(CourseApi::class.java)
    }

    @Provides
    fun provideReportApi(): ReportApi {
        return RetrofitService()
            .getRetrofit(educationAppApi, true)
            .create(ReportApi::class.java)
    }

    @Provides
    fun providerChatApi(): ChatApi {
        return RetrofitService()
            .getRetrofit(educationAppApi, true)
            .create(ChatApi::class.java)
    }

    @Provides
    fun providerDirectionApi(): DirectionApi {
        return RetrofitService()
            .getRetrofit(educationAppApi, true)
            .create(DirectionApi::class.java)
    }
}