package ge.mjavarchik.messenger.model.mappers

import ge.mjavarchik.messenger.model.api.Message
import ge.mjavarchik.messenger.model.data.MessageEntity
import java.text.SimpleDateFormat
import java.util.*

class MessageMapper {

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    fun toEntity(message: Message?, hashedPassword: String): MessageEntity? {
        return message?.let {
            MessageEntity(
                it.from,
                dateFormatter.format(it.date),
                it.message
            )
        }
    }

    fun fromEntity(messageEntity: MessageEntity?, to: String): Message? {
        return messageEntity?.let {
            val from = it.sender ?: return null
            val message = it.text ?: return null
            val timestamp = it.timestamp ?: return null
            val date = dateFormatter.parse(timestamp) ?: return null
            Message(
                from,
                to,
                message,
                date
            )
        }
    }
}