package ge.mjavarchik.messenger.model.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ConversationEntity(
    var user1: String? = null,
    var user2: String? = null,
    var messages: Map<String, MessageEntity>? = null
)