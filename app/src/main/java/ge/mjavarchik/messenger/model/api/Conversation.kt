package ge.mjavarchik.messenger.model.api

import java.util.*


data class Conversation(var chatId: String? = null,
                        var date: Date? = null,
                        var friendsAvatar: String? = null,
                        val messageText: String? = null,
                        var from: String? = null,
                        var to: String? = null
) : Comparable<Conversation> {
    override fun compareTo(other: Conversation): Int {
        return date!!.compareTo(other.date)
    }
}
