package ge.mjavarchik.messenger.viewmodel

import androidx.lifecycle.*
import ge.mjavarchik.messenger.model.repository.FirebaseRepository
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class LogInViewModel(private val repository: FirebaseRepository): ViewModel() {

    private var _signedIn = MutableLiveData<Boolean>()
    val signedIn: LiveData<Boolean> get() = _signedIn
    private var accountUsername: String = "No username"
    private var accountProfession: String = "No profession"
    fun signInUser(nickname: String, password: String){
        viewModelScope.launch {
            val userEntity = repository.getUser(nickname)
            userEntity?.let {
                val isPasswordCorrect = BCrypt.checkpw(password, it.hashedPassword)
                accountUsername = userEntity.nickname
                accountProfession = userEntity.profession
                _signedIn.postValue(isPasswordCorrect)
            } ?: run {
                _signedIn.postValue(false)
            }
        }
    }

    fun getUserName(): String = accountUsername

    fun getProfession(): String = accountProfession

    companion object {
        fun getViewModelFactory(): LogInViewModelFactory {
            return LogInViewModelFactory()
        }
    }
}

class LogInViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LogInViewModel(FirebaseRepository()) as T
    }
}