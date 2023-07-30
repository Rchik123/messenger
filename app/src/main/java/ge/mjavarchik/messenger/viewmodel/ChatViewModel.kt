package ge.mjavarchik.messenger.viewmodel

import android.content.Context
import androidx.lifecycle.*
import ge.mjavarchik.messenger.model.api.User
import ge.mjavarchik.messenger.model.data.ConversationEntity
import ge.mjavarchik.messenger.model.data.MessageEntity
import ge.mjavarchik.messenger.model.mappers.UserMapper
import ge.mjavarchik.messenger.model.repository.ConversationFirebaseRepository
import ge.mjavarchik.messenger.model.repository.FirebaseRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ChatViewModel(
    private val conversationFirebaseRepository: ConversationFirebaseRepository,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val userMapper = UserMapper()

    private val _conversation = MutableLiveData<ConversationEntity>()
    val conversation: LiveData<ConversationEntity> get() = _conversation
    private var _receiverUser = MutableLiveData<User>()
    val receiverUser: LiveData<User> get() = _receiverUser

    fun listenToConversation(user1: String, user2: String) {
        conversationFirebaseRepository.listenToConversation(user1, user2)
        conversationFirebaseRepository.conversationLiveData.observeForever { conversationEntity ->
            _conversation.postValue(conversationEntity)
        }
    }

    fun sendMessage(sender: String, receiver: String, text: String) = viewModelScope.launch {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = sdf.format(Date())
        conversationFirebaseRepository.addMessageToConversation(sender, receiver,
            MessageEntity(sender, currentDate, text)
        )
    }

    fun setReceiverUser(username: String){
        viewModelScope.launch {
            _receiverUser.postValue(
                userMapper.fromEntity(
                    firebaseRepository.getUserByUsername(username)
                )
            )
        }
    }

    companion object {
        fun getViewModelFactory(context: Context): ChatViewModelFactory {
            return ChatViewModelFactory(context)
        }
    }
}

class ChatViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(
            ConversationFirebaseRepository(context),
            FirebaseRepository(context)
        ) as T
    }
}