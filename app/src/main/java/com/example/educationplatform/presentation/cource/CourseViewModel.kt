package com.example.educationplatform.presentation.cource

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.educationplatform.domain.entities.*
import com.example.educationplatform.domain.interactors.*
import com.example.educationplatform.presentation.cource.modules.adapter.ModuleItem
import com.example.educationplatform.presentation.cource.reports.adapter.ReportItem
import com.example.educationplatform.utils.USER_NOT_LOADED_EXCEPTION
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant
import java.util.*

class CourseViewModel constructor(
    private val courseId: Int,
    private val loadCourseInteractor: LoadCourseInteractor,
    private val loadTakesModulesInteractor: LoadTakesModulesInteractor,
    private val loadCourseReportsInteractor: LoadCourseReportsInteractor,
    private val checkUserInCourseInteractor: CheckUserInCourseInteractor,
    private val loadCurrentUserInteractor: LoadCurrentUserInteractor,
    private val sendReportInteractor: SendReportInteractor,
    private val subscribeInteractor: SubscribeInteractor
) : ViewModel() {

    private var subscription: Boolean = false
    private var user: User? = null

    private val _subscriptionFlow = MutableSharedFlow<Boolean>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _courseFlow = MutableSharedFlow<Course>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _reportsFlow = MutableStateFlow<List<ReportItem>>(emptyList())
    private val _modulesFlow = MutableStateFlow<List<ModuleItem>>(emptyList())
    private val _errorFlow = MutableStateFlow(false)
    private val _processFlow = MutableStateFlow(false)

    val subscriptionFlow = _subscriptionFlow.asSharedFlow()
    val courseFlow = _courseFlow.asSharedFlow()
    val reportsFlow = _reportsFlow.asStateFlow()
    val modulesFlow = _modulesFlow.asStateFlow()
    val errorFlow = _errorFlow.asStateFlow()
    val processFlow = _processFlow.asStateFlow()

    init {
        loadData()
    }

    fun sendReport(message: String, rating: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                user?.let { user ->
                    val report = Report(
                        -1,
                        user.name,
                        user.icon,
                        message,
                        rating,
                        courseId,
                        Date.from(Instant.now())
                    )
                    launch {
                        sendReportInteractor(report)
                    }
                    launch {
                        val reports = _reportsFlow.value.toMutableList()
                        reports.add(1, ReportItem.UserReportItem(report))
                        _reportsFlow.emit(reports)
                    }
                }
            }
        }
    }

    fun subscribeProcess() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (user != null) {
                    subscribeInteractor(subscription, courseId).onSuccess {
                        subscription = !subscription
                        _subscriptionFlow.emit(subscription)
                        updateReports()
                        updateModulesWithSubscription()
                    }
                }
            }
        }
    }

    fun updateStage(newTakesStage: TakesStage) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val modules = _modulesFlow.value.toMutableList()
                val moduleIndex = modules.indexOfFirst { moduleItem ->
                    (moduleItem as? ModuleItem.TakesModuleItem)?.module?.id == newTakesStage.moduleId
                }
                var updatedModule = modules[moduleIndex] as? ModuleItem.TakesModuleItem
                updatedModule?.module?.stages?.replaceAll { takesStage ->
                    if (takesStage.id == newTakesStage.id) {
                        newTakesStage
                    } else {
                        takesStage
                    }
                }
                updatedModule = updatedModule?.let { moduleItem ->
                    val module =
                        moduleItem.module.copy(userScore = moduleItem.module.stages.fold(0) { acc, takesStage ->
                            acc + takesStage.userScore
                        })
                    moduleItem.copy(module = module)
                }
                updatedModule?.let {
                    //Изменяем текущий модуль
                    modules.removeAt(moduleIndex)
                    modules.add(moduleIndex, it)
                    //Обновляем сдедующий модуль
                    if (moduleIndex < modules.count() - 1) {
                        val canCheckNext = it.module.userScore >= it.module.totalScore
                        val module = modules.removeAt(moduleIndex + 1)
                        modules.add(moduleIndex + 1, (module as ModuleItem.TakesModuleItem).copy(checkable =  canCheckNext))
                    }
                }
                _modulesFlow.emit(modules)
            }
        }
    }

    private fun loadData() {
        try {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    //Загрузка пользователя, необходимо для дальнейшей работы с подпиской и загрузкой молулей
                    loadCurrentUserInteractor().onSuccess { user ->
                        this@CourseViewModel.user = user
                    }.onFailure {
                        _errorFlow.emit(true)
                        this.cancel()
                    }

                    val subscriptionProcess = loadSubscription()
                    val courseProcess = loadCourse()
                    val reportsProcess = loadReports()
                    val modulesProcess = loadModules()

                    subscriptionProcess.await().onSuccess { subscription ->
                        this@CourseViewModel.subscription = subscription
                        _subscriptionFlow.emit(subscription)
                        prepareCourse(courseProcess.await())
                        prepareReports(reportsProcess.await())
                        prepareModules(modulesProcess.await())
                    }
                }
            }
        } catch (e: Exception) {
            val x = e
        }

    }

    private fun updateReports() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (subscription) {
                    val newReports = mutableListOf<ReportItem>(ReportItem.AddReportItem(user!!))
                    newReports.addAll(_reportsFlow.value)
                    _reportsFlow.emit(newReports)
                } else {
                    val newReports = if (_reportsFlow.value.count() > 1) {
                        _reportsFlow.value.subList(1, _reportsFlow.value.count() - 1)
                    } else {
                        emptyList()
                    }
                    _reportsFlow.emit(newReports)
                }
            }
        }
    }

    private fun updateModulesWithSubscription() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var canMove = true
                val newModules = _modulesFlow.value.mapIndexed { index, moduleItem ->
                    canMove =
                        canMove && ((index == 0) || ((_modulesFlow.value[index - 1] as ModuleItem.TakesModuleItem).module.userScore >= (_modulesFlow.value[index - 1] as ModuleItem.TakesModuleItem).module.totalScore))
                    (moduleItem as ModuleItem.TakesModuleItem).copy(checkable = subscription && canMove)
                }
                _modulesFlow.emit(newModules)
            }
        }
    }

    private fun loadCourse() = viewModelScope.async {
        withContext(Dispatchers.IO) {
            loadCourseInteractor.invoke(courseId)
        }
    }

    private fun prepareCourse(courseResult: Result<Course>) = viewModelScope.launch {
        courseResult.onSuccess { course ->
            _courseFlow.emit(course)
        }
    }

    private fun loadSubscription() = viewModelScope.async {
        withContext(Dispatchers.IO) {
            user?.let { user ->
                checkUserInCourseInteractor(courseId)
            } ?: Result.failure(Exception(USER_NOT_LOADED_EXCEPTION))
        }
    }

    private fun loadReports() = viewModelScope.async {
        withContext(Dispatchers.IO) {
            loadCourseReportsInteractor(courseId)
        }
    }

    private suspend fun prepareReports(reportsResult: Result<List<Report>>) {
        user?.let { user ->
            reportsResult.onSuccess { reports ->
                val reportsItems = mutableListOf<ReportItem>()
                if (subscription) {
                    reportsItems.add(ReportItem.AddReportItem(user))
                }
                reportsItems.addAll(reports.map {
                    ReportItem.UserReportItem(it)
                })
                _reportsFlow.emit(reportsItems)
            }

        }
    }

    private fun loadModules() = viewModelScope.async {
        withContext(Dispatchers.IO) {
            user?.let { user ->
                loadTakesModulesInteractor(courseId)
            } ?: Result.failure(Exception(USER_NOT_LOADED_EXCEPTION))
        }
    }

    private suspend fun prepareModules(modulesResult: Result<List<TakesModule>>) {
        modulesResult.onSuccess { modules ->
            var canMove = true
            _modulesFlow.emit(modules.mapIndexed { index, takesModule ->
                canMove =
                    canMove && ((index == 0) || (modules[index - 1].userScore >= modules[index - 1].totalScore))
                ModuleItem.TakesModuleItem(takesModule, subscription && canMove)
            })
        }
    }

    class CourseViewModelFactory @AssistedInject constructor(
        @Assisted private val courseId: Int,
        private val loadCourseInteractor: LoadCourseInteractor,
        private val loadTakesModulesInteractor: LoadTakesModulesInteractor,
        private val loadCourseReportsInteractor: LoadCourseReportsInteractor,
        private val checkUserInCourseInteractor: CheckUserInCourseInteractor,
        private val loadCurrentUserInteractor: LoadCurrentUserInteractor,
        private val sendReportInteractor: SendReportInteractor,
        private val subscribeInteractor: SubscribeInteractor
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CourseViewModel(
                courseId,
                loadCourseInteractor,
                loadTakesModulesInteractor,
                loadCourseReportsInteractor,
                checkUserInCourseInteractor,
                loadCurrentUserInteractor,
                sendReportInteractor,
                subscribeInteractor
            ) as T
        }

        @AssistedFactory
        interface Factory {
            fun create(@Assisted courseId: Int): CourseViewModelFactory
        }
    }
}