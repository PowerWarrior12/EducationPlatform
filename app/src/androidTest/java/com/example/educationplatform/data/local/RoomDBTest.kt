package com.example.educationplatform.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before

abstract class RoomDBTest {
    private var _db: RoomDB? = null

    val db: RoomDB
        get() = checkNotNull(_db)

    @Before
    fun init() {
        _db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RoomDB::class.java
        ).build()
    }

    @After
    fun close() {
        db.close()
    }
}