package ge.mjavarchik.messenger.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.mjavarchik.messenger.databinding.IncommingMessageBinding
import ge.mjavarchik.messenger.databinding.OutMessageBinding
import ge.mjavarchik.messenger.model.api.Message

class ChatAdapter(
    private val loggedInUser: String,
    private val messages: ArrayList<Message>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MessageType.OUT.value) {
            OutMessageViewHolder(OutMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            InMessageViewHolder(IncommingMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OutMessageViewHolder) {
            holder.tvMessage.text = messages[position].message
//            holder.tvDate.text = messages[position].date.toString()
        } else if (holder is InMessageViewHolder) {
            holder.tvMessage.text = messages[position].message
//            holder.tvDate.text = messages[position].date.toString() // TODO: add date formatting
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].from == loggedInUser) {
            MessageType.OUT.value
        } else {
            MessageType.IN.value
        }
    }
}

class InMessageViewHolder(
    binding: IncommingMessageBinding
) : RecyclerView.ViewHolder(binding.root) {
    val tvMessage: TextView = binding.textMessage
    val tvDate: TextView = binding.timeTxt
}

class OutMessageViewHolder(
    binding: OutMessageBinding
) : RecyclerView.ViewHolder(binding.root) {
    val tvMessage: TextView = binding.messageTextOut
    val tvDate: TextView = binding.timeText
}

enum class MessageType(val value: Int) {
    OUT(0),
    IN(1)
}