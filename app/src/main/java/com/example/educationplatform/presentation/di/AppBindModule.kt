package com.example.educationplatform.presentation.di

import com.example.educationplatform.data.repositoriesImpl.*
import com.example.educationplatform.domain.repositories.*
import dagger.Binds
import dagger.Module

@Module
abstract class AppBindModule {

    @Binds
    abstract fun bindUserRepositoryLocal(repository: UserRepositoryLocalPlug): UserRepositoryLocal

    @Binds
    abstract fun bindUserRepositoryRemote(repository: UserRepositoryRemotePlug): UserRepositoryRemote

}