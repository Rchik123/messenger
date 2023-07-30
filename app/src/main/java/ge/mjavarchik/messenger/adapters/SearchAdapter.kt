package ge.mjavarchik.messenger.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ge.mjavarchik.messenger.R
import ge.mjavarchik.messenger.databinding.AllChatsPageBinding
import ge.mjavarchik.messenger.databinding.SearchResultsBinding
import ge.mjavarchik.messenger.model.api.Conversation
import ge.mjavarchik.messenger.model.api.User
import ge.mjavarchik.messenger.view.activity.SearchActivity

class SearchAdapter (var list: MutableList<User>,
                     var searchActivity: SearchActivity,
                     private val listener: OnSearchItemClickListener
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {


    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SearchResultsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(list[holder.adapterPosition].avatar != null && list[holder.adapterPosition].avatar != ""){
            Glide.with(searchActivity).load(list[holder.adapterPosition].avatar).circleCrop().into(holder.img)
        } else {
            holder.img.setImageResource(R.drawable.avatar_image_placeholder)
        }
        holder.userName.text = list[position].nickname
        holder.prof.text = list[position].profession
        holder.itemView.setOnClickListener{
            listener.onSearchItemClick(list[holder.adapterPosition])
        }
    }


    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(binding: SearchResultsBinding) : RecyclerView.ViewHolder(binding.root) {
        var img: ImageView = binding.profileImage
        var userName: TextView = binding.searchNick
        var prof: TextView = binding.searchProfession
    }

    interface OnSearchItemClickListener {
        fun onSearchItemClick(user: User)
    }
}