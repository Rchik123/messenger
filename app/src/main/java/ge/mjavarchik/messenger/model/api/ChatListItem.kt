package ge.mjavarchik.messenger.model.api

import java.util.*


data class ChatListItem(
    var chatId: String? = null,
    var date: Date? = null,
    var friendsAvatar: String? = null,
    val messageText: String? = null
    )