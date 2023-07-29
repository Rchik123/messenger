package ge.mjavarchik.messenger.model.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class MessageEntity(
    var sender: String? = null,
    var timestamp: String? = null,
    var text: String? = null
)