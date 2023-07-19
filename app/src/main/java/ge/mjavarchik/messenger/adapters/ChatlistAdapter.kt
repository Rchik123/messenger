package ge.mjavarchik.messenger.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ge.mjavarchik.messenger.R
import ge.mjavarchik.messenger.databinding.ConversationItemsBinding
import ge.mjavarchik.messenger.model.api.ChatListItem
import ge.mjavarchik.messenger.model.api.User
import java.util.*

//shesacvlelia chatis itemze

class ChatlistAdapter(private var context: Context, private var userName: String,
                      var list: List<ChatListItem>) :
    RecyclerView.Adapter<ChatlistAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ConversationItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var friendsAvatarString = list[holder.adapterPosition].friendsAvatar
        if(friendsAvatarString == null || friendsAvatarString == ""){
            holder.profileImage.setImageResource(R.drawable.avatar_image_placeholder)
        } else if(friendsAvatarString != null && friendsAvatarString != "") {

        }

        val date = list[holder.adapterPosition].date?.let{
            convertTime(it)
        }
    }
    private fun convertTime(currDate: Date){

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

}
