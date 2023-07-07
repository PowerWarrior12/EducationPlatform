package com.example.educationplatform.di

import com.example.educationplatform.data.repositoriesImpl.*
import com.example.educationplatform.domain.repositories.*
import dagger.Binds
import dagger.Module

@Module
abstract class AppBindModule {

    @Binds
    abstract fun bindUserRepositoryLocal(repository: UserRepositoryLocalImpl): UserRepositoryLocal
    @Binds
    abstract fun bindUserRepositoryRemote(repository: UserRepositoryRemoteImpl): UserRepositoryRemote
    @Binds
    abstract fun bindCoursesRepositoryRemote(repository: CourseRepositoryRemoteImpl): CoursesRepositoryRemote
    @Binds
    abstract fun bindReportRepositoryRemote(repository: ReportRepositoryRemoteImpl): ReportRepositoryRemote
    @Binds
    abstract fun bindChatRepositoryRemote(repository: ChatRepositoryRemoteImpl): ChatRepositoryRemote
    @Binds
    abstract fun bindCodeExecutionRepository(repository: CodeExecutionRepositoryImpl): CodeExecutionRepository
    @Binds
    abstract fun bindDeviceRepository(repository: DeviceRepositoryImpl): DeviceRepository
}