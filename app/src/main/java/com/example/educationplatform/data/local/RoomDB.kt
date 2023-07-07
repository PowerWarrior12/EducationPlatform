package com.example.educationplatform.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.educationplatform.data.local.user.UserDao
import com.example.educationplatform.data.local.user.entities.UserEntity

const val DB_VERSION = 1

@Database(entities = [UserEntity::class], version = DB_VERSION)
abstract class RoomDB: RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        fun getDatabase(appContext: Context): RoomDB {
            synchronized(this) {
                return Room.databaseBuilder(
                    appContext,
                    RoomDB::class.java,
                    RoomDB::class.java.simpleName
                )
                    .build()
            }
        }
    }
}