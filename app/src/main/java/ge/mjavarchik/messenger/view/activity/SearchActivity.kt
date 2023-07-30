package ge.mjavarchik.messenger.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import ge.mjavarchik.messenger.adapters.ChatListAdapter
import ge.mjavarchik.messenger.adapters.SearchAdapter
import ge.mjavarchik.messenger.databinding.AllChatsPageBinding
import ge.mjavarchik.messenger.databinding.SearchLayoutBinding
import ge.mjavarchik.messenger.model.api.Conversation
import ge.mjavarchik.messenger.model.api.User
import ge.mjavarchik.messenger.model.data.UserEntity
import ge.mjavarchik.messenger.viewmodel.LoggedInViewModel
import ge.mjavarchik.messenger.viewmodel.LoggedInViewModelFactory

class SearchActivity : AppCompatActivity() {
    private val list = mutableListOf<User>()

    private val viewModel: LoggedInViewModel by viewModels {
        LoggedInViewModelFactory(this)
    }
    private lateinit var binding: SearchLayoutBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var searchRecView: RecyclerView
    private lateinit var currUser: User
    private lateinit var adapter: SearchAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
        viewModel.allUsers.observe(this, Observer {
            if (it != null) {
                updateInfo(it)
            }
        })
        viewModel.loggedInUser.observe(this) {
            if (it != null) {
                currUser = it
            }
        }
        adapter = SearchAdapter(list, this, object : SearchAdapter.OnSearchItemClickListener {
            override fun onSearchItemClick(user: User) {
                val intent = Intent(this@SearchActivity, ChatActivity::class.java).apply {
                    putExtra("sender", currUser.username)
                    putExtra("receiver", user.username)
                }
                startActivity(intent)
            }
        })
        binding.friends.adapter = adapter
        binding.friends.visibility = View.INVISIBLE


    }

    private fun updateInfo(it: List<UserEntity>) {
        val newList = mutableListOf<User>()
        for (eachUser in it) {
            if (eachUser.username == currUser.username) continue
            newList.add(
                User(
                    eachUser.username,
                    eachUser.nickname,
                    eachUser.profession,
                    eachUser.avatar
                )
            )
        }
        progressBar.visibility = View.GONE
        binding.friends.visibility = View.VISIBLE
        adapter.list = newList
        adapter.notifyDataSetChanged()
        if(newList.isEmpty()){
            Toast.makeText(this, "No user", Toast.LENGTH_SHORT).show()
        }

    }

    fun goBack(view: View) {
        finish()
    }

    companion object {
        const val usern = "current username"
        const val nck = "current nickname"
    }
}
