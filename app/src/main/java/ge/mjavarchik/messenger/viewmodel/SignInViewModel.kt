package ge.mjavarchik.messenger.viewmodel

import androidx.lifecycle.*
import ge.mjavarchik.messenger.model.repository.FirebaseRepository
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class SignInViewModel(private val repository: FirebaseRepository): ViewModel() {
    private var _signedIn = MutableLiveData<Boolean>()
    val signedIn: LiveData<Boolean> get() = _signedIn


    fun signInUser(nickname: String, password: String){
        viewModelScope.launch {
            val userEntity = repository.getUser(nickname)
            if (userEntity != null) {
                val isPasswordCorrect = BCrypt.checkpw(password, userEntity.hashedPassword)
                if(isPasswordCorrect){
                    _signedIn.postValue(true)
                } else {
                    _signedIn.postValue(false)
                }
            } else{
                _signedIn.postValue(false)
            }
        }
    }


    companion object {
        fun getViewModelFactory(): SignInViewModelFactory {
            return SignInViewModelFactory()
        }
    }
}

class SignInViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignInViewModel(FirebaseRepository()) as T
    }
}