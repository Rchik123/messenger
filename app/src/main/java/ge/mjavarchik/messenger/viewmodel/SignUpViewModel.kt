package ge.mjavarchik.messenger.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ge.mjavarchik.messenger.model.api.User
import ge.mjavarchik.messenger.model.data.UserEntity
import ge.mjavarchik.messenger.model.repository.FirebaseRepository
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class SignUpViewModel(private val repository: FirebaseRepository) : ViewModel() {

    private var enteredUsername: String = ""

    private var _signedUp = MutableLiveData<Boolean>()
    val signedUp: LiveData<Boolean> get() = _signedUp

    fun signUpUser(user: User, password: String) {
        viewModelScope.launch {
            val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
            val userEntity = UserEntity(user.username, user.nickname, user.profession, "", hashedPassword)
            enteredUsername = userEntity.username
            _signedUp.postValue(repository.addUser(userEntity))
        }
    }

    fun getEnteredUsername(): String = enteredUsername

    companion object {
        fun getViewModelFactory(): SignUpViewModelFactory {
            return SignUpViewModelFactory()
        }
    }
}

class SignUpViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignUpViewModel(FirebaseRepository()) as T
    }
}