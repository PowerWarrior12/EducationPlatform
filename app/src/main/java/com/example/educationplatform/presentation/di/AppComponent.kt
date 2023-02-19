package com.example.educationplatform.presentation.di

import com.example.educationplatform.presentation.authorization.AuthorizationFragment
import com.example.educationplatform.presentation.registration.RegistrationFragment
import dagger.Component

@Component(modules = [AppBindModule::class])
interface AppComponent {
    fun inject(fragment: AuthorizationFragment)
    fun inject(fragment: RegistrationFragment)
}