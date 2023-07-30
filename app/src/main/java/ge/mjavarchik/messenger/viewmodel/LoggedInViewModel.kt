package ge.mjavarchik.messenger.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.*
import ge.mjavarchik.messenger.model.api.Conversation
import ge.mjavarchik.messenger.model.api.User
import ge.mjavarchik.messenger.model.data.MessageEntity
import ge.mjavarchik.messenger.model.data.UserEntity
import ge.mjavarchik.messenger.model.mappers.MessageMapper
import ge.mjavarchik.messenger.model.mappers.UserMapper
import ge.mjavarchik.messenger.model.repository.ConversationFirebaseRepository
import ge.mjavarchik.messenger.model.repository.FirebaseRepository
import ge.mjavarchik.messenger.model.repository.LogInPreferenceRepository
import kotlinx.coroutines.launch

class LoggedInViewModel(
    private val firebaseRepository: FirebaseRepository,
    private val conversationFirebaseRepository: ConversationFirebaseRepository,
    private val preferenceRepository: LogInPreferenceRepository,
    private val userMapper: UserMapper
) : ViewModel() {

    private val messageMapper = MessageMapper()

    private var _loggedInUser = MutableLiveData<User>()
    val loggedInUser: LiveData<User> get() = _loggedInUser
    private val _allUsers = MutableLiveData<List<UserEntity>>()
    val allUsers: LiveData<List<UserEntity>> get() = _allUsers
    private val _allConversations = MutableLiveData<List<Conversation>>()
    val allConversations: LiveData<List<Conversation>> get() = _allConversations

    init {
        viewModelScope.launch {
            _loggedInUser.postValue(getLoggedInUser())
        }
        loggedInUser.observeForever {
            listenToAllUsers()
        }
        allUsers.observeForever {
            listenToAllConversations()
        }
    }

    fun updateUserInformation(newNickname: String, newProfession: String, newAvatar: Bitmap?) {
        viewModelScope.launch {
            _loggedInUser.value?.let {
                firebaseRepository.updateUser(it.username, newNickname, newProfession, newAvatar)
            }
        }
    }

    private fun listenToAllUsers() {
        firebaseRepository.usersLiveData.observeForever { users ->
            _allUsers.postValue(users)
        }
    }

    private fun listenToAllConversations() {
        conversationFirebaseRepository.allConversationsLiveData.observeForever { conversationEntities ->
            val conversations = conversationEntities.mapNotNull { entity ->
                val otherUsername = if (entity.user1 == loggedInUser.value?.username) {
                    entity.user2
                } else if (entity.user2 == loggedInUser.value?.username) {
                    entity.user1
                } else {
                    null
                }
                val otherUserEntity = allUsers.value?.find { it.username == otherUsername }
                val otherUser = otherUserEntity?.let { userMapper.fromEntity(it) }
                val lastMessageEntity = entity.messages?.let { getLastMessageEntity(it) }
                otherUser?.let { user ->
                    val lastMessage = messageMapper.fromEntity(lastMessageEntity, "")
                    lastMessage?.let { message ->
                        Conversation(user, message)
                    }
                }
            }
            _allConversations.postValue(conversations)
        }
    }

    fun signOut() {
        preferenceRepository.clearLoggedInUsername()
    }

    private fun getLastMessageEntity(messages: Map<String, MessageEntity>): MessageEntity? {
        var latestMessage: MessageEntity? = null
        var latestTimestamp = ""

        messages.values.forEach { message ->
            message.timestamp?.let {
                if (latestMessage == null || it > latestTimestamp) {
                    latestMessage = message
                    latestTimestamp = it
                }
            }
        }

        return latestMessage
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
            FirebaseRepository(context),
            ConversationFirebaseRepository(context),
            LogInPreferenceRepository(context),
            UserMapper()
        ) as T
    }
}