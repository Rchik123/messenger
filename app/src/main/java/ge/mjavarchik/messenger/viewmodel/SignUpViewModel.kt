package ge.mjavarchik.messenger.viewmodel

import android.util.Log
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

    private var _signedUp = MutableLiveData<Boolean>()
    val signedUp: LiveData<Boolean> get() = _signedUp

    init {
        viewModelScope.launch {
            _signedUp.value = false
        }
    }

    fun registerUser(user: User) {
        viewModelScope.launch {
            val hashedPassword = BCrypt.hashpw(user.password, BCrypt.gensalt())
            val userEntity = UserEntity(user.nickname, user.profession, hashedPassword)
            Log.d("asd123", userEntity.toString())
            if (repository.addUser(userEntity)) _signedUp.postValue(true)
        }
    }

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