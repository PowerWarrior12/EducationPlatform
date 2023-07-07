package com.example.educationplatform.data.repositoriesImpl

import com.example.educationplatform.data.local.PreferencesStorage
import com.example.educationplatform.data.local.RoomDB
import com.example.educationplatform.data.local.user.UserDao
import com.example.educationplatform.data.mappers.toUser
import com.example.educationplatform.data.mappers.toUserEntity
import com.example.educationplatform.domain.entities.User
import com.example.educationplatform.utils.TestUtil
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.*

@DisplayName("test local user repository")
internal class UserRepositoryLocalImplTest {

    private lateinit var repository: UserRepositoryLocalImpl
    private val dao: UserDao = mock()
    private val preferencesStorage: PreferencesStorage = mock()
    @BeforeEach
    fun init() {
        val db: RoomDB = mock()
        Mockito.`when`(db.userDao()).thenReturn(dao)
        repository = UserRepositoryLocalImpl(db, preferencesStorage)
    }

    @Test
    @DisplayName("test save user on local bd success")
    fun saveUser(): Unit = runBlocking {
        val userStatus = "STATUS"
        val inputUser = User.default().copy(status = userStatus)
        val userEntities = listOf(TestUtil.createUserEntity())
        Mockito.`when`(dao.getUsers()).thenReturn(userEntities)
        Mockito.`when`(dao.updateUser(any())).thenReturn(1)

        val actual = repository.saveUser(inputUser)
        val expected = Result.success(Unit)

        Mockito.verify(dao).getUsers()
        Mockito.verify(preferencesStorage).saveStatus(userStatus)
        Mockito.verify(dao).updateUser(inputUser.toUserEntity().copy(id = userEntities.first().id))

        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test getting user success")
    fun getUser(): Unit = runBlocking {
        val resultUser = TestUtil.createUserEntity()
        val userEntities = listOf(resultUser)
        Mockito.`when`(dao.getUsers()).thenReturn(userEntities)

        val actual = repository.getUser()
        val expected = Result.success(resultUser.toUser())

        Mockito.verify(dao).getUsers()

        Assertions.assertEquals(expected, actual)
    }

    @Test
    @DisplayName("test getting user status success")
    fun getStatus(): Unit = runBlocking {
        val expected = "STATUS"
        Mockito.`when`(preferencesStorage.getStatus()).thenReturn(expected)

        val actual = repository.getStatus()

        Mockito.verify(preferencesStorage).getStatus()
        Assertions.assertEquals(expected, actual)
    }
}