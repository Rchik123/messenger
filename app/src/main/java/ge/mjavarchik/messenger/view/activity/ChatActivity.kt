package ge.mjavarchik.messenger.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ge.mjavarchik.messenger.databinding.ActivityChatBinding
import ge.mjavarchik.messenger.model.api.Message
import ge.mjavarchik.messenger.adapters.ChatAdapter
import java.util.Date

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpRvAdapter()
        setCollapsedToolbarListener()
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

    private fun setUpRvAdapter() {
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