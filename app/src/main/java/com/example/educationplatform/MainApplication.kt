package com.example.educationplatform

import android.app.Application
import com.example.educationplatform.di.AppComponent
import com.example.educationplatform.di.DaggerAppComponent
import io.atreydos.okhttpcookiestorage.CookieStorage
import java.net.CookieHandler
import java.net.CookieManager

class MainApplication: Application() {

    private var _appComponent: AppComponent? = null

    val appComponent: AppComponent
        get() = checkNotNull(_appComponent) {
            "AppComponent isn't initialized"
        }

    override fun onCreate() {
        super.onCreate()
        //Setup cookie manager
        val cookieManager = CookieManager()
        CookieHandler.setDefault(cookieManager)
        CookieStorage.init(this)
        //Setup dagger component
        _appComponent = DaggerAppComponent.create()
        INSTANCE = this
    }

    companion object {
        lateinit var INSTANCE: MainApplication
            private set
    }

}