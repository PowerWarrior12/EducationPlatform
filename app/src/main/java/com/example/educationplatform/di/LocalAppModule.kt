package com.example.educationplatform.di

import android.content.Context
import com.example.educationplatform.MainApplication
import com.example.educationplatform.data.local.RoomDB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalAppModule {
    @Singleton
    @Provides
    fun provideRoomDB(context: Context): RoomDB {
        return RoomDB.getDatabase(context)
    }

    @Provides
    fun provideContext(): Context {
        return MainApplication.INSTANCE
    }
}