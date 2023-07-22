package ge.mjavarchik.messenger.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ge.mjavarchik.messenger.adapters.ChatListAdapter
import ge.mjavarchik.messenger.databinding.AllChatsPageBinding
import ge.mjavarchik.messenger.model.api.Message
import java.util.*

class UserHomePageFragment : Fragment() {
    private lateinit var _binding : AllChatsPageBinding
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
        chatRecView =  _binding.conversations
        var list = arrayListOf<Message>(
            Message(
                "1",
                Date(),
                "",
                "Ragac mesiji",
                "Mariam",
                "Sxvisi saxeli"
            )
        )
        chatRecView.adapter = ChatListAdapter(this, "vigac", list)

        //ragac
        progressBar.visibility = View.GONE

    }
}