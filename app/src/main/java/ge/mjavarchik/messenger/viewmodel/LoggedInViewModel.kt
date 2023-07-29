package ge.mjavarchik.messenger.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.*
import ge.mjavarchik.messenger.model.api.User
import ge.mjavarchik.messenger.model.mappers.UserMapper
import ge.mjavarchik.messenger.model.repository.FirebaseRepository
import ge.mjavarchik.messenger.model.repository.LogInPreferenceRepository
import kotlinx.coroutines.launch

class LoggedInViewModel(
    private val firebaseRepository: FirebaseRepository,
    private val preferenceRepository: LogInPreferenceRepository,
    private val userMapper: UserMapper
) : ViewModel() {

    private var _loggedInUser = MutableLiveData<User>()
    val loggedInUser: LiveData<User> get() = _loggedInUser

    init {
        viewModelScope.launch {
            _loggedInUser.postValue(getLoggedInUser())
        }
    }

    fun updateUserInformation(newNickname: String, newProfession: String, newAvatar: Bitmap?) {
        viewModelScope.launch {
            _loggedInUser.value?.let {
                firebaseRepository.updateUser(it.username, newNickname, newProfession, newAvatar)
            }
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
            FirebaseRepository(),
            LogInPreferenceRepository(context),
            UserMapper()
        ) as T
    }
}