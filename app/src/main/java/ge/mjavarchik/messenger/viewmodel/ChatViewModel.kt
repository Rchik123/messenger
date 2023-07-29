package ge.mjavarchik.messenger.viewmodel

import androidx.lifecycle.*
import ge.mjavarchik.messenger.model.data.ConversationEntity
import ge.mjavarchik.messenger.model.data.MessageEntity
import ge.mjavarchik.messenger.model.repository.ConversationFirebaseRepository
import kotlinx.coroutines.launch

class ChatViewModel(
    private val conversationFirebaseRepository: ConversationFirebaseRepository,
) : ViewModel() {

    private val _conversation = MutableLiveData<ConversationEntity>()
    val conversation: LiveData<ConversationEntity> get() = _conversation

    fun listenToConversation(user1: String, user2: String) {
        conversationFirebaseRepository.listenToConversation(user1, user2)
        conversationFirebaseRepository.conversationLiveData.observeForever { conversationEntity ->
            _conversation.postValue(conversationEntity)
        }
    }

    fun sendMessage(sender: String, receiver: String, text: String) = viewModelScope.launch {
        conversationFirebaseRepository.addMessageToConversation(sender, receiver,
            MessageEntity(sender, null, text)
        )
    }


    companion object {
        fun getViewModelFactory(): ChatViewModelFactory {
            return ChatViewModelFactory()
        }
    }
}

class ChatViewModelFactory(
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(
            ConversationFirebaseRepository()
        ) as T
    }
}