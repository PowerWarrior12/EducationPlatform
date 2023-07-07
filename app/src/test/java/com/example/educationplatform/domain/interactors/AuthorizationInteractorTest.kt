package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.User
import com.example.educationplatform.domain.repositories.DeviceRepository
import com.example.educationplatform.domain.repositories.UserRepositoryLocal
import com.example.educationplatform.domain.repositories.UserRepositoryRemote
import com.example.educationplatform.utils.ecxeptions.ConnectionException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class AuthorizationInteractorTest {
    private val deviceRepositoryMock = mock<DeviceRepository>()
    private val userRepositoryRemoteMock = mock<UserRepositoryRemote>()
    private val userRepositoryLocalMock = mock<UserRepositoryLocal>()

    private var authorizationInteractor = AuthorizationInteractor(
        deviceRepositoryMock,
        userRepositoryRemoteMock,
        userRepositoryLocalMock
    )

    private val emailInput = "businessmail1710@mail.ru"
    private val passwordInput = "1234567"

    @AfterEach
    fun afterEach() {
        Mockito.clearInvocations(
            deviceRepositoryMock,
            userRepositoryRemoteMock,
            userRepositoryLocalMock
        )
    }

    @ParameterizedTest(name = "Internet connection : {0}")
    @ValueSource(booleans = [true, false])
    @DisplayName("authorization test internet connection")
    fun authorizationTestInternetConnection(internetConnection: Boolean) {
        runBlocking {

            val userResult = Result.success(
                User.default()
            )
            val authorizationResultSuccess = Result.success(Unit)

            Mockito.`when`(deviceRepositoryMock.checkInternetConnection())
                .thenReturn(internetConnection)

            Mockito.`when`(userRepositoryRemoteMock.authorization(emailInput, passwordInput))
                .thenReturn(userResult)

            Mockito.`when`(userRepositoryLocalMock.saveUser(any())).thenReturn(authorizationResultSuccess)

            val expected = if (internetConnection) Result.success(Unit) else Result.failure(ConnectionException())

            val actual = authorizationInteractor.run(emailInput, passwordInput)

            if (internetConnection) {
                verify(userRepositoryRemoteMock).authorization(emailInput, passwordInput)
            }

            Assertions.assertEquals(expected, actual)
        }
    }
}