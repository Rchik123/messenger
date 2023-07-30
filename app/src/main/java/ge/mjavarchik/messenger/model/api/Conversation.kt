package ge.mjavarchik.messenger.model.api

data class Conversation(
    val user: User,
    val lastMessage: Message
) : Comparable<Conversation> {
    override fun compareTo(other: Conversation): Int {
        return lastMessage.date.compareTo(other.lastMessage.date)
    }
}
