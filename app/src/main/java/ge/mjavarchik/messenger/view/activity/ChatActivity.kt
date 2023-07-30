package ge.mjavarchik.messenger.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import ge.mjavarchik.messenger.databinding.ActivityChatBinding
import ge.mjavarchik.messenger.model.api.Message
import ge.mjavarchik.messenger.adapters.ChatAdapter
import ge.mjavarchik.messenger.model.mappers.MessageMapper
import ge.mjavarchik.messenger.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private val viewModel: ChatViewModel by viewModels {
        ChatViewModel.getViewModelFactory()
    }
    private lateinit var binding: ActivityChatBinding

    private lateinit var sender: String
    private lateinit var receiver: String

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val messageMapper = MessageMapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sender = intent.getStringExtra("sender").toString()
        receiver = intent.getStringExtra("receiver").toString()

        setCollapsedToolbarListener()
        setUpSendButtonListener()
        setUpConversationObserver()
    }

    private fun setUpConversationObserver() {
        viewModel.listenToConversation(sender, receiver)
        viewModel.conversation.observe(this) { conversationEntity ->
            val messageList = arrayListOf<Message>()
            conversationEntity.messages?.let { map ->
                for ((_, messageEntity) in map) {
                    val message = messageMapper.fromEntity(messageEntity, receiver)
                    message?.let {
                        messageList.add(it)
                    }
                }
            }
            binding.recyclerView.adapter = ChatAdapter(
                sender,
                ArrayList(messageList.sortedBy { it.date })
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