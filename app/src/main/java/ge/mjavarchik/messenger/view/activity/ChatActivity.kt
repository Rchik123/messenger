package ge.mjavarchik.messenger.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import ge.mjavarchik.messenger.databinding.ActivityChatBinding
import ge.mjavarchik.messenger.model.api.Message
import ge.mjavarchik.messenger.adapters.ChatAdapter
import ge.mjavarchik.messenger.viewmodel.ChatViewModel
import java.util.Date

class ChatActivity : AppCompatActivity() {

    private val viewModel: ChatViewModel by viewModels {
        ChatViewModel.getViewModelFactory()
    }
    private lateinit var binding: ActivityChatBinding

    private val sender: String = "rezi"
    private val receiver: String = "mariam"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCollapsedToolbarListener()
        setUpSendButtonListener()
        setUpConversationObserver()
    }

    private fun setUpConversationObserver() {
        viewModel.listenToConversation(sender, receiver)
        viewModel.conversation.observe(this) {
            val messageList = arrayListOf<Message>()
            it.messages?.let { map ->
                for ((key, value) in map) {
                    val entity = value
                    val message = Message(
                        entity.sender,
                        receiver,
                        entity.text,
                        null
                    )
                    messageList.add(message)
                }
            }

            binding.recyclerView.adapter = ChatAdapter(
                sender,
                messageList
            )
        }
    }

    private fun setCollapsedToolbarListener() {
        binding.clAbl.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (appBarLayout.totalScrollRange + verticalOffset == 0) {
                binding.toolbar.visibility = View.VISIBLE
                binding.collapsingToolbarBack.visibility = View.INVISIBLE
                binding.collapsingToolbarNickname.visibility = View.INVISIBLE
                binding.collapsingToolbarProfession.visibility = View.INVISIBLE
                binding.collapsingToolbarPfp.visibility = View.INVISIBLE
            } else {
                binding.toolbar.visibility = View.INVISIBLE
                binding.collapsingToolbarBack.visibility = View.VISIBLE
                binding.collapsingToolbarNickname.visibility = View.VISIBLE
                binding.collapsingToolbarProfession.visibility = View.VISIBLE
                binding.collapsingToolbarPfp.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpSendButtonListener() {
        binding.ibEnterMessage.setOnClickListener {
            val editText = binding.etEnterMessage
            val text = editText.text.toString()
            editText.text.clear()
            viewModel.sendMessage(sender, receiver, text)
        }
    }
}