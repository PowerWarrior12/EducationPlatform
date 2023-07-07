package com.example.educationplatform.data.local

import com.example.educationplatform.utils.TestUtil
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class UserDaoTest: RoomDBTest() {

    @Test
    fun createAndReadUserTest(): Unit = runBlocking {
        val user = TestUtil.createUserEntity()
        db.userDao().createUser(user)

        val loaded = db.userDao().getUsers().firstOrNull { it == user }
        Assert.assertEquals(user, loaded)
    }

    @Test
    fun updateUserTest(): Unit = runBlocking {
        val user = TestUtil.createUserEntity()
        db.userDao().createUser(user)

        val loadedUser = db.userDao().getUsers().firstOrNull { it == user }
        Assert.assertNotNull(loadedUser)
        val newName = "new_name"
        val updatedUser = loadedUser!!.copy(name = newName)

        val actualCount = db.userDao().updateUser(updatedUser)
        val expectedCount = 1
        Assert.assertEquals(expectedCount, actualCount)
    }

    @Test
    fun deleteUserTest(): Unit = runBlocking {
        val user = TestUtil.createUserEntity()
        db.userDao().createUser(user)

        val loadedUser = db.userDao().getUsers().firstOrNull { it == user }
        Assert.assertNotNull(loadedUser)

        val actualCount = db.userDao().deleteUser(user)
        val expectedCount = 1

        Assert.assertEquals(expectedCount, actualCount)
    }
}