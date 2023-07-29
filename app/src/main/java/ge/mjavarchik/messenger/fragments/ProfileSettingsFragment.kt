package ge.mjavarchik.messenger.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ge.mjavarchik.messenger.databinding.ActivityProfilePageBinding
import ge.mjavarchik.messenger.viewmodel.LoggedInViewModel
import ge.mjavarchik.messenger.viewmodel.LoggedInViewModelFactory

class ProfileSettingsFragment : Fragment() {

    private val viewModel: LoggedInViewModel by viewModels {
        LoggedInViewModelFactory(requireActivity().application)
    }

    private var _binding: ActivityProfilePageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityProfilePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.visibility = View.VISIBLE
        setUpImageUpload()
        setUpLoggedInUserObserver()
        setUpUpdateBtnListener()
        setUpSignOutBtnListener()
    }

    private fun setUpImageUpload() {
        binding.ivProfileAvatar.setOnClickListener {
            openGalleryForImage()
        }
    }

    private val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            binding.ivProfileAvatar.scaleType = ImageView.ScaleType.CENTER_CROP
            Glide.with(this)
                .load(uri)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.ivProfileAvatar)
        }
    }
    private fun openGalleryForImage() {
        imagePicker.launch("image/*")
    }


    private fun setUpSignOutBtnListener() {
        binding.btnSignOut.setOnClickListener {
            viewModel.signOut()
            activity?.finish()
        }
    }
    private var avatarStr = ""
    private fun setUpLoggedInUserObserver() {
        viewModel.loggedInUser.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                binding.etUsername.setText(it.nickname)
                binding.etProfession.setText(it.profession)
                this.avatarStr = it.avatar.toString()
                if (it.avatar != "") {
                    Glide.with(this)
                        .load(it.avatar)
                        .circleCrop()
                        .into(binding.ivProfileAvatar)
                }
            }
            binding.progressBar.visibility = View.GONE
        })
    }


    private fun setUpUpdateBtnListener() {
        binding.btnUpdate.setOnClickListener {
            viewModel.updateUserInformation(
                binding.etUsername.text.toString(),
                binding.etProfession.text.toString(),
                binding.ivProfileAvatar.drawable.toBitmap()
            )
        }
        binding.progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}