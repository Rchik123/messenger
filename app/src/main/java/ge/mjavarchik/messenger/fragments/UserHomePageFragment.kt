package ge.mjavarchik.messenger.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import ge.mjavarchik.messenger.model.repository.LogInPreferenceRepository
import ge.mjavarchik.messenger.view.activity.ChatActivity
import ge.mjavarchik.messenger.viewmodel.LoggedInViewModel
import ge.mjavarchik.messenger.viewmodel.LoggedInViewModelFactory
import java.util.*

class UserHomePageFragment : Fragment() {

    private lateinit var preferences: LogInPreferenceRepository

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
        preferences = LogInPreferenceRepository(requireActivity().applicationContext)
        progressBar = _binding.progressBar
        progressBar.visibility = View.VISIBLE
        chatRecView = _binding.conversations
        setUpAllUserObserver()
        Log.d("asd", preferences.toString())
        //ragac
        progressBar.visibility = View.GONE
    }

    private fun setUpAllUserObserver() {
        viewModel.listenToAllUsers()
        viewModel.allUsers.observe(this) {
            val conversationList = arrayListOf<Conversation>()
            for (userEntity in it) {
                conversationList.add(
                    Conversation(
                        "id",
                        Date(),
                        userEntity.avatar,
                        "message",
                        userEntity.username,
                        userEntity.username
                    )
                )
            }
            chatRecView.adapter = ChatListAdapter(
                this,
                "vigac",
                conversationList,
                object : ChatListAdapter.OnItemClickListener {
                    override fun onItemClick(conversation: Conversation) {
                        val intent = Intent(requireContext(), ChatActivity::class.java)
                        val sender = preferences?.getLoggedInUsername()
                        val receiver = conversation.from
                        intent.putExtra("sender", sender)
                        intent.putExtra("receiver", receiver)
                        startActivity(intent)
                    }
                }
            )
        }
    }
}
