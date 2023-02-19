package com.example.educationplatform.presentation.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.educationplatform.domain.interactors.AuthorizationInteractor
import com.example.educationplatform.utils.emailValidation
import com.example.educationplatform.utils.passwordValidation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthorizationViewModel private constructor(
    private val authorizationInteractor: AuthorizationInteractor
): ViewModel() {
    private var login: String = ""
    private var password: String = ""

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
                val result = authorizationInteractor.run(login, password)
                result.onSuccess {

                }
                result.onFailure {

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