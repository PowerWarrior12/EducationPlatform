package com.example.educationplatform.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.educationplatform.domain.entities.User
import com.example.educationplatform.domain.interactors.ChangeStatusInteractor
import com.example.educationplatform.domain.interactors.LoadCurrentUserInteractor
import com.example.educationplatform.domain.interactors.UpdateUserInteractor
import com.example.educationplatform.utils.emailValidation
import com.example.educationplatform.utils.nameValidation
import com.example.educationplatform.utils.passwordValidation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileViewModel(
    private val loadCurrentUserInteractor: LoadCurrentUserInteractor,
    private val updateUserInteractor: UpdateUserInteractor,
    private val changeStatusInteractor: ChangeStatusInteractor
): ViewModel() {

    private var name: String = ""
    private var secondName: String = ""
    private var information: String = ""
    private var currentPassword: String = ""
    private var newPassword: String = ""
    private var email: String = ""

    private val _userFlow = MutableSharedFlow<User>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _errorFlow = MutableStateFlow(false)
    private val _processFlow = MutableStateFlow(false)
    private val _userUpdatedFlow = MutableSharedFlow<Unit>(0, 1, BufferOverflow.DROP_OLDEST)
    private val _requestToSendStatusFlow = MutableSharedFlow<Unit>(0, 1, BufferOverflow.DROP_OLDEST)

    val userFlow = _userFlow.asSharedFlow()
    val errorFlow = _errorFlow.asStateFlow()
    val processFlow = _processFlow.asStateFlow()
    val userUpdatedFlow = _userUpdatedFlow.asSharedFlow()
    val requestToSendStatusFlow = _requestToSendStatusFlow.asSharedFlow()

    init {
        loadUser()
    }

    fun setName(newName: String): Boolean {
        name = newName
        return nameValidation(newName)
    }

    fun setSecondName(newSecondName: String): Boolean {
        secondName = newSecondName
        return nameValidation(newSecondName)
    }

    fun setInfo(newInformation: String): Boolean {
        information = newInformation
        return true
    }

    fun setCurrentPassword(newCurrentPassword: String): Boolean {
        currentPassword = newCurrentPassword
        return true
    }

    fun setNewPassword(newNewPassword: String): Boolean {
        newPassword = newNewPassword
        return passwordValidation(newNewPassword)
    }

    fun setEmail(newEmail: String): Boolean {
        email = newEmail
        return emailValidation(newEmail)
    }

    fun updatePersonalInformation() {
        if (nameValidation(name) && nameValidation(secondName)) {
            viewModelScope.launch {
                userFlow.firstOrNull { true }?.let { user ->
                    updateUser(user.copy(name = name, secondName = secondName, info = information))
                }
            }
        }
    }

    fun updatePassword() {
        viewModelScope.launch {
            userFlow.firstOrNull { true }?.let { user ->
                if (user.password == currentPassword && passwordValidation(newPassword)) {
                    updateUser(user.copy(password = newPassword))
                }
            }
        }
    }

    fun updateEmail() {
        viewModelScope.launch {
            userFlow.firstOrNull { true }?.let { user ->
                if (emailValidation(email)) {
                    updateUser(user.copy(email = email))
                }
            }
        }
    }

    fun sendChangeStatusRequest(message: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                changeStatusInteractor(message).onSuccess {
                    _requestToSendStatusFlow.emit(Unit)
                }.onFailure {
                    _errorFlow.emit(true)
                }
            }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _processFlow.emit(true)
                loadCurrentUserInteractor().onSuccess { user ->
                    _userFlow.emit(user)
                }.onFailure {
                    _errorFlow.emit(true)
                }
                _processFlow.emit(true)
            }
        }
    }

    private fun updateUser(user: User) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _processFlow.emit(true)
                updateUserInteractor(user).onSuccess {
                    _userFlow.emit(user)
                    _userUpdatedFlow.emit(Unit)
                }.onFailure {
                    _errorFlow.emit(true)
                }
                _processFlow.emit(false)
            }
        }
    }

    class ProfileViewModelFactory @Inject constructor(
        private val loadCurrentUserInteractor: LoadCurrentUserInteractor,
        private val updateUserInteractor: UpdateUserInteractor,
        private val changeStatusInteractor: ChangeStatusInteractor
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel(
                loadCurrentUserInteractor, updateUserInteractor, changeStatusInteractor
            ) as T
        }
    }
}