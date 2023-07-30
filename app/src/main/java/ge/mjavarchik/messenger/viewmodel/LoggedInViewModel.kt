package ge.mjavarchik.messenger.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.*
import ge.mjavarchik.messenger.model.api.User
import ge.mjavarchik.messenger.model.data.UserEntity
import ge.mjavarchik.messenger.model.mappers.UserMapper
import ge.mjavarchik.messenger.model.repository.FirebaseRepository
import ge.mjavarchik.messenger.model.repository.LogInPreferenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoggedInViewModel(
    private val firebaseRepository: FirebaseRepository,
    private val preferenceRepository: LogInPreferenceRepository,
    private val userMapper: UserMapper
) : ViewModel() {

    private var _loggedInUser = MutableLiveData<User>()
    val loggedInUser: LiveData<User> get() = _loggedInUser
    private val _allUsers = MutableLiveData<List<UserEntity>>()
    val allUsers: LiveData<List<UserEntity>> get() = _allUsers
    var isLastPage = false
    var isLoading = false
    private var currentPage = 1

    init {
        viewModelScope.launch {
            _loggedInUser.postValue(getLoggedInUser())
        }
        listenToAllUsers()

    }


    fun updateUserInformation(newNickname: String, newProfession: String, newAvatar: Bitmap?) {
        viewModelScope.launch {
            _loggedInUser.value?.let {
                firebaseRepository.updateUser(it.username, newNickname, newProfession, newAvatar)
            }
        }
    }


//    fun getUsersByNickname(nickname: String): LiveData<List<UserEntity>> {
//        return firebaseRepository.getUsersByNickname(nickname)
//    }

    private fun listenToAllUsers() {
        firebaseRepository.usersLiveData.observeForever { users ->
            _allUsers.postValue(users)
        }
    }

    fun signOut() {
        preferenceRepository.clearLoggedInUsername()
    }

    private suspend fun getLoggedInUser(): User? {
        val loggedInUsername = preferenceRepository.getLoggedInUsername()
        return loggedInUsername?.let {
            userMapper.fromEntity(firebaseRepository.getUserByUsername(it))
        }
    }

    companion object {
        private const val PAGE_SIZE = 6
        fun getViewModelFactory(context: Context): LoggedInViewModelFactory {
            return LoggedInViewModelFactory(context)
        }
    }
}

class LoggedInViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoggedInViewModel(
            FirebaseRepository(context),
            LogInPreferenceRepository(context),
            UserMapper()
        ) as T
    }
}