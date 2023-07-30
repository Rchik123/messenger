package ge.mjavarchik.messenger.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ge.mjavarchik.messenger.adapters.SearchAdapter
import ge.mjavarchik.messenger.databinding.SearchLayoutBinding
import ge.mjavarchik.messenger.model.api.User
import ge.mjavarchik.messenger.model.data.UserEntity
import ge.mjavarchik.messenger.viewmodel.LoggedInViewModel
import ge.mjavarchik.messenger.viewmodel.LoggedInViewModelFactory
import kotlinx.coroutines.delay

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
    private var isLoading = false
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

        binding.searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                if (searchText.length >= 3) {
                    getUsersByNickname(searchText)
                } else {
                    getUsersByNickname("")
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun getUsersByNickname(nickname: String) {

        progressBar.visibility = View.VISIBLE


        viewModel.allUsers.observe(this, Observer {
            progressBar.visibility = View.GONE
            if (it != null) {
                if(nickname != ""){
                    updateInfoNicks(it, nickname)
                } else {
                    updateInfo(it)
                }

            }else {
                Toast.makeText(this, "Error retrieving users", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun updateInfoNicks(it: List<UserEntity>, nick: String){
        val newList = mutableListOf<User>()
        for(eachUser in it) {
            if (eachUser.username == currUser.username) continue
            if (eachUser.nickname.contains(nick)){
                newList.add(
                    User(
                        eachUser.username,
                        eachUser.nickname,
                        eachUser.profession,
                        eachUser.avatar
                    )
                )
            }
        }
        progressBar.visibility = View.GONE
        binding.friends.visibility = View.VISIBLE
        adapter.list = newList
        adapter.notifyDataSetChanged()
        if(newList.isEmpty()){
            Toast.makeText(this, "No user", Toast.LENGTH_SHORT).show()
        }
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
