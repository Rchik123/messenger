package ge.mjavarchik.messenger.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import ge.mjavarchik.messenger.adapters.ChatListAdapter
import ge.mjavarchik.messenger.databinding.AllChatsPageBinding
import ge.mjavarchik.messenger.model.api.Conversation
import ge.mjavarchik.messenger.view.activity.ChatActivity
import ge.mjavarchik.messenger.viewmodel.LoggedInViewModel
import ge.mjavarchik.messenger.viewmodel.LoggedInViewModelFactory

class UserHomePageFragment : Fragment() {

    private val viewModel: LoggedInViewModel by viewModels {
        LoggedInViewModelFactory(requireActivity().application)
    }

    private lateinit var _binding: AllChatsPageBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var chatRecView: RecyclerView
    private lateinit var currUserName: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AllChatsPageBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = _binding.progressBar
        progressBar.visibility = View.VISIBLE
        chatRecView = _binding.conversations
        _binding.searchText.addTextChangedListener(object : TextWatcher {
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
        setUpAllConversationObserver()
        progressBar.visibility = View.GONE
    }

    private fun getUsersByNickname(nick: String) {
        progressBar.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        if (nick != "") {
            updateInfoNicks(nick)
        } else {
            viewModel.allConversations.value?.let {
                setConversationAdapter(it)
            }
        }
    }

    private fun updateInfoNicks(nick: String) {
        val conversations = mutableListOf<Conversation>()
        viewModel.allConversations.value?.forEach {
            if (it.user.nickname.contains(nick)) {
                conversations.add(it)
            }
        }
        setConversationAdapter(conversations)
    }


    private fun setUpAllConversationObserver() {
        viewModel.allConversations.observe(this) {
            setConversationAdapter(it)
        }
    }

    private fun setConversationAdapter(conversations: List<Conversation>) {
        chatRecView.adapter = ChatListAdapter(
            this,
            conversations.sortedByDescending() { conversation -> conversation.lastMessage.date },
            object : ChatListAdapter.OnItemClickListener {
                override fun onItemClick(conversation: Conversation) {
                    val intent = Intent(requireContext(), ChatActivity::class.java).apply {
                        putExtra("sender", viewModel.loggedInUser.value?.username)
                        putExtra("receiver", conversation.user.username)
                    }
                    startActivity(intent)
                }
            }
        )
    }
}
