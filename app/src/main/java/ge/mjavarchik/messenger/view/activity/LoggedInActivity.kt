package ge.mjavarchik.messenger.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ge.mjavarchik.messenger.databinding.ActivityProfilePageBinding
import ge.mjavarchik.messenger.viewmodel.LoggedInViewModel

class LoggedInActivity : AppCompatActivity() {

    private val viewModel: LoggedInViewModel by viewModels {
        LoggedInViewModel.getViewModelFactory(applicationContext)
    }

    private lateinit var binding: ActivityProfilePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpLoggedInUserObserver()
        setUpUpdateBtnListener()
    }

    private fun setUpLoggedInUserObserver() {
        viewModel.loggedInUser.observe(this) {
            binding.etUsername.setText(it.nickname)
            binding.etProfession.setText(it.profession)
        }
    }

    private fun setUpUpdateBtnListener() {
        binding.btnUpdate.setOnClickListener {
            viewModel.updateUserInformation(
                binding.etUsername.text.toString(),
                binding.etProfession.text.toString()
            )
        }
    }
}