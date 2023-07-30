package ge.mjavarchik.messenger.viewmodel

import android.content.Context
import androidx.lifecycle.*
import ge.mjavarchik.messenger.model.repository.FirebaseRepository
import ge.mjavarchik.messenger.model.repository.LogInPreferenceRepository
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class LogInViewModel(
    private val firebaseRepository: FirebaseRepository,
    private val preferenceRepository: LogInPreferenceRepository
) : ViewModel() {

    private var _signedIn = MutableLiveData<Boolean>()
    val signedIn: LiveData<Boolean> get() = _signedIn

    fun wasAlreadyLoggedIn(): Boolean{
        return preferenceRepository.getLoggedInUsername() != null
    }


    fun signInUser(username: String, password: String) {
        viewModelScope.launch {
            val userEntity = firebaseRepository.getUserByUsername(username)
            userEntity?.let {
                val isPasswordCorrect = BCrypt.checkpw(password, it.hashedPassword)
                if (isPasswordCorrect) {
                    preferenceRepository.setLoggedInUsername(it.username)
                }
                _signedIn.postValue(isPasswordCorrect)
            } ?: run {
                _signedIn.postValue(false)
            }
        }
    }

    companion object {
        fun getViewModelFactory(context: Context): LogInViewModelFactory {
            return LogInViewModelFactory(context)
        }
    }
}

class LogInViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LogInViewModel(FirebaseRepository(context), LogInPreferenceRepository(context)) as T
    }
}