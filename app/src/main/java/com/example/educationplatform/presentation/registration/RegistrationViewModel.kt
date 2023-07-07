package com.example.educationplatform.presentation.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.educationplatform.domain.entities.User
import com.example.educationplatform.domain.interactors.RegistrationInteractor
import com.example.educationplatform.utils.emailValidation
import com.example.educationplatform.utils.nameValidation
import com.example.educationplatform.utils.passwordValidation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationViewModel(
    private val registrationInteractor: RegistrationInteractor
) : ViewModel() {

    private val _processFlow = MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST)
    private val _errorFlow = MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST)
    private val _onRegistrationFlow = MutableSharedFlow<Unit>(0, 1, BufferOverflow.DROP_OLDEST)

    val processFlow = _processFlow.asSharedFlow()
    val errorFlow = _errorFlow.asSharedFlow()
    val onRegistrationFlow = _onRegistrationFlow.asSharedFlow()

    private var name: String = ""
    private var secondName: String = ""
    private var login: String = ""
    private var password: String = ""
    private var replyPassword: String = ""

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
     * @return Возвращает результат валидации повтора пароля
     */
    fun updateReplyPassword(newPassword: String): Boolean {
        replyPassword = newPassword
        return replyPassword == password
    }

    /**
     * @return Возвращает результат валидации имени
     */
    fun updateName(newName: String): Boolean {
        name = newName
        return nameValidation(newName)
    }

    /**
     * @return Возвращает результат валидации имени
     */
    fun updateSecondName(newSecondName: String): Boolean {
        secondName = newSecondName
        return nameValidation(newSecondName)
    }

    /**
     * Метод для регистрации, при возникновении ошибки оповещает представление
     */
    fun registration() {
        if (emailValidation(login) && passwordValidation(password) && nameValidation(name) && nameValidation(
                secondName
            ) && password == replyPassword
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                _processFlow.emit(true)
                registrationInteractor(
                    User(
                        name = name,
                        secondName = secondName,
                        password = password,
                        email = login
                    )
                )
                    .onSuccess {
                        _onRegistrationFlow.emit(Unit)
                    }.onFailure {
                        _errorFlow.emit(true)
                    }
                _processFlow.emit(false)
            }
        }
    }

    class RegistrationViewModelFactory @Inject constructor(
        private val registrationInteractor: RegistrationInteractor
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RegistrationViewModel(registrationInteractor) as T
        }
    }
}