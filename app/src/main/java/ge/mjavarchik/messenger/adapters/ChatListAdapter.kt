package ge.mjavarchik.messenger.adapters

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ge.mjavarchik.messenger.R
import ge.mjavarchik.messenger.databinding.ConversationItemsBinding
import ge.mjavarchik.messenger.fragments.UserHomePageFragment
import ge.mjavarchik.messenger.model.api.Conversation
import java.util.*


class ChatListAdapter(
    private var context: UserHomePageFragment,
    var list: List<Conversation>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ConversationItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    // binds the list items to a view
    //listener aqvs dasamatebeli chatze dacherisas rom eg chat gaixsnas
    //axlandeli user rom aris chartuli eg

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var friendsAvatarString = list[holder.adapterPosition].user.avatar
        if (friendsAvatarString == null || friendsAvatarString == "") {
            holder.profileImage.setImageResource(R.drawable.avatar_image_placeholder)
        } else if (friendsAvatarString != "") {
            Glide.with(context).load(friendsAvatarString).circleCrop().into(holder.profileImage)
        }
        val date = list[holder.adapterPosition].lastMessage.date
        var dateStr = ""
        if (date != null) {
            dateStr = convertTime(date)
        }
        holder.time.text = dateStr
        holder.message.text = list[holder.adapterPosition].lastMessage.message
        holder.itemView.setOnClickListener{
            listener.onItemClick(list[holder.adapterPosition])
        }
        holder.nickname.text = list[holder.adapterPosition].user.nickname
    }

    private fun convertTime(date: Date): String {
        val difference = Calendar.getInstance().timeInMillis - date.time
        val secDifference = difference / 1000
        // Calculate time difference in minutes, hours, and days
        val minutes = (difference / (1000 * 60)).toInt()
        val hours = (difference / (1000 * 60 * 60)).toInt()
        val days = (difference / (1000 * 60 * 60 * 24)).toInt()

        return when {

            minutes < 1 && hours < 1 -> "$secDifference sec"
            hours < 1 -> "$minutes min"
            hours in 2..23 -> "$hours hours"
            hours < 24 -> "$hours hour"
            else -> {
                // Format the date as '5 JUN'
                val dateFormat = SimpleDateFormat("d MMM", Locale.getDefault())
                dateFormat.format(date)
            }
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(binding: ConversationItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        var time = binding.date
        var nickname = binding.searchNick
        var message = binding.searchMessages
        var profileImage = binding.profileImage
    }

    interface OnItemClickListener {
        fun onItemClick(conversation: Conversation)
    }
}