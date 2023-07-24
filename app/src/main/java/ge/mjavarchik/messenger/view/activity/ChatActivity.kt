package ge.mjavarchik.messenger.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ge.mjavarchik.messenger.databinding.ActivityChatBinding
import ge.mjavarchik.messenger.model.api.Message
import ge.mjavarchik.messenger.view.activity.rv.ChatAdapter
import java.util.Date

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpRvAdapter()
    }

    private fun setUpRvAdapter() {
        Log.d("shevida aq", "diax")
        binding.recyclerView.adapter = ChatAdapter(
            "user1",
            arrayListOf(
                Message("user1", "user2", "gamarjoba", Date(1000)),
                Message("user2", "user1", "gamarjoba aseve", Date(1000)),
                Message("user1", "user2", "Lorem ipsum dolor sit amet.", Date(1000)),
                Message("user1", "user2", "ohooooo", Date(1000)),
                Message("user1", "user2", "ohooooooooooo", Date(1000)),
                Message("user2", "user1", "aaaaaaaa", Date(1000)),
                Message("user2", "user1", "aaaaaaaa", Date(1000)),
                Message("user2", "user1", "aaaaaaaa", Date(1000)),
                Message("user2", "user1", "aaaaaaaa", Date(1000))
            )
        )
    }
}