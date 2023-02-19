package com.example.educationplatform

import android.app.Application
import com.example.educationplatform.presentation.di.AppComponent
import com.example.educationplatform.presentation.di.DaggerAppComponent

class MainApplication: Application() {

    private var _appComponent: AppComponent? = null

    val appComponent: AppComponent
        get() = checkNotNull(_appComponent) {
            "AppComponent isn't initialized"
        }

    override fun onCreate() {
        super.onCreate()
        _appComponent = DaggerAppComponent.create()
        INSTANCE = this
    }

    companion object {
        lateinit var INSTANCE: MainApplication
            private set
    }

}