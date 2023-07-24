package ge.mjavarchik.messenger.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ge.mjavarchik.messenger.databinding.ActivityProfilePageBinding
import ge.mjavarchik.messenger.viewmodel.LoggedInViewModel
import ge.mjavarchik.messenger.viewmodel.LoggedInViewModelFactory
import java.util.*

class ProfileSettingsFragment : Fragment() {
    private lateinit var _binding : ActivityProfilePageBinding
    private lateinit var progressBar: ProgressBar
    private var userInfo = ""
    private var nicknameInfo = ""
    private var avatarInfo = ""
    private var professionInfo = ""
    private lateinit var usernameText: TextView
    private lateinit var imageview : ImageView
    private lateinit var professionText: TextView
    private lateinit var updateButton: Button
    private lateinit var signOutButton: Button

    private val viewModel: LoggedInViewModel by lazy {
        ViewModelProvider(requireActivity(), LoggedInViewModelFactory(requireActivity().application))[LoggedInViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityProfilePageBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = _binding.progressBar
        progressBar.visibility = View.VISIBLE

        this.imageview = _binding.ivProfileAvatar
        this.usernameText = _binding.etUsername
        this.professionText = _binding.etProfession
        //ragac
        this.imageview.setOnClickListener {
            uploadImage(it)
        }
        progressBar.visibility = View.GONE



    }

    private fun uploadImage(it: View?) {

    }
    private fun setUpSignOutBtnListener() {
        _binding.btnSignOut.setOnClickListener {
            viewModel.signOut()
        }
    }

    private fun setUpLoggedInUserObserver() {
        viewModel.loggedInUser.observe(this) {
            _binding.etUsername.setText(it.nickname)
            _binding.etProfession.setText(it.profession)
        }
    }

    private fun setUpUpdateBtnListener() {
        _binding.btnUpdate.setOnClickListener {
            viewModel.updateUserInformation(
                _binding.etUsername.text.toString(),
                _binding.etProfession.text.toString()
            )
        }
    }

}