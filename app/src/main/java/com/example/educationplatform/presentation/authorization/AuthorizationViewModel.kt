package com.example.educationplatform.presentation.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.educationplatform.domain.interactors.AuthorizationInteractor
import com.example.educationplatform.utils.emailValidation
import com.example.educationplatform.utils.passwordValidation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthorizationViewModel private constructor(
    private val authorizationInteractor: AuthorizationInteractor
): ViewModel() {
    private var login: String = ""
    private var password: String = ""
    private val _authorizationFlow = MutableSharedFlow<Unit>(0, 1, BufferOverflow.DROP_OLDEST)
    private val _errorFlow = MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST)
    private val _processFlow = MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST)

    val authorizationFlow = _authorizationFlow.asSharedFlow()
    val errorFlow = _errorFlow.asSharedFlow()
    val processFlow = _processFlow.asSharedFlow()

    /**
     * @return Возвращает результат валидации логина
     */
    fun updateLogin(newLogin: String): Boolean {
        login = newLogin
        return emailValidation(login)
    }

    /**
     * @return Возвращает результат валидации пароля
     */
    fun updatePassword(newPassword: String): Boolean {
        password = newPassword
        return passwordValidation(newPassword)
    }

    /**
     * Метод для авторизации, при возникновении ошибки оповещает представление
     */
    fun authorization() {
        if (emailValidation(login) && passwordValidation(password)) {
            viewModelScope.launch(Dispatchers.IO) {
                _processFlow.emit(true)
                val result = authorizationInteractor.run(login, password)
                _processFlow.emit(false)
                result.onSuccess {
                    _authorizationFlow.emit(Unit)
                }
                result.onFailure {
                    _errorFlow.emit(true)
                }
            }
        }
    }

    class AuthorizationViewModelFactory @Inject constructor(
        private val authorizationInteractor: AuthorizationInteractor
    ): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthorizationViewModel(
                authorizationInteractor
            ) as T
        }
    }
}