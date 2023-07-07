package com.example.educationplatform.di

import com.example.educationplatform.presentation.authorization.AuthorizationFragment
import com.example.educationplatform.presentation.catalog.CatalogFragment
import com.example.educationplatform.presentation.chat.ChatFragment
import com.example.educationplatform.presentation.chat.messenger.MessengerFragment
import com.example.educationplatform.presentation.cource.CourseFragment
import com.example.educationplatform.presentation.cource.module.blockDiagram.BlockDiagramDisplayFragment
import com.example.educationplatform.presentation.cource.module.coding.CodingDisplayFragment
import com.example.educationplatform.presentation.courseEdit.CourseEditFragment
import com.example.educationplatform.presentation.courseEdit.structure.blockStageCreate.BlockStageCreateFragment
import com.example.educationplatform.presentation.courseEdit.structure.codeStageCreate.CodeStageCreateFragment
import com.example.educationplatform.presentation.courseEdit.structure.lectureStageCreate.LectureStageCreateFragment
import com.example.educationplatform.presentation.courseEdit.structure.moduleCreate.ModuleCreateDialogFragment
import com.example.educationplatform.presentation.createCourse.CourseCreateFragment
import com.example.educationplatform.presentation.home.HomeFragment
import com.example.educationplatform.presentation.profile.ProfileFragment
import com.example.educationplatform.presentation.registration.RegistrationFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppBindModule::class, RemoteAppModule::class, LocalAppModule::class])
interface AppComponent {
    fun inject(fragment: AuthorizationFragment)
    fun inject(fragment: RegistrationFragment)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: CourseCreateFragment)
    fun inject(fragment: CourseEditFragment)
    fun inject(fragment: LectureStageCreateFragment)
    fun inject(fragment: CodeStageCreateFragment)
    fun inject(fragment: ModuleCreateDialogFragment)
    fun inject(fragment: BlockStageCreateFragment)
    fun inject(fragment: CatalogFragment)
    fun inject(fragment: CourseFragment)
    fun inject(fragment: CodingDisplayFragment)
    fun inject(fragment: BlockDiagramDisplayFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: ChatFragment)
    fun inject(fragment: MessengerFragment)
}