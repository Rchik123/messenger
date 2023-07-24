package ge.mjavarchik.messenger.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
        setUpLoggedInUserObserver()
        setUpUpdateBtnListener()
        setUpSignOutBtnListener()
    }

    private fun setUpSignOutBtnListener() {
        binding.btnSignOut.setOnClickListener {
            viewModel.signOut()
            activity?.finish()
        }
    }

    private fun setUpLoggedInUserObserver() {
        viewModel.loggedInUser.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                binding.etUsername.setText(it.nickname)
                binding.etProfession.setText(it.profession)
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun setUpUpdateBtnListener() {
        binding.btnUpdate.setOnClickListener {
            viewModel.updateUserInformation(
                binding.etUsername.text.toString(),
                binding.etProfession.text.toString()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}