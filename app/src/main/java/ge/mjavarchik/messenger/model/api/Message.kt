package ge.mjavarchik.messenger.model.api

import java.util.*

data class Message(var chatId: String? = null,
                   var date: Date? = null,
                   var friendsAvatar: String? = null,
                   val messageText: String? = null,
                   var from: String? = null,
                   var to: String? = null
) : Comparable<Message> {
    override fun compareTo(other: Message): Int {
        return date!!.compareTo(other.date)
    }
}
