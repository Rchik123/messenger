package ge.mjavarchik.messenger.fragments

import android.content.Intent
import android.os.Bundle
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
        setUpAllConversationObserver()
        progressBar.visibility = View.GONE
    }

    private fun setUpAllConversationObserver() {
        viewModel.allConversations.observe(this) {
            chatRecView.adapter = ChatListAdapter(
                this,
                it.sortedByDescending() { conversation -> conversation.lastMessage.date },
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
}
