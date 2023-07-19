package ge.mjavarchik.messenger.view.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ge.mjavarchik.messenger.databinding.SignUpLayoutBinding
import ge.mjavarchik.messenger.viewmodel.SignUpViewModel
import androidx.activity.viewModels
import ge.mjavarchik.messenger.model.api.User

class SignUpActivity : AppCompatActivity() {

    private val viewModel: SignUpViewModel by viewModels {
        SignUpViewModel.getViewModelFactory()
    }
    private lateinit var binding: SignUpLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignUpLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpSignedUpObserver()
        setUpSignUpBtnListener()
        setUpProfessionEtListener()
    }

    private fun setUpSignedUpObserver() {
        viewModel.signedUp.observe(this) { signedUp ->
            if (signedUp) {
                Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Username: ${viewModel.getEnteredUsername()} already exists",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setUpSignUpBtnListener() {
        binding.signUpButton.setOnClickListener {
            val nickSignUp: EditText = binding.nickSignUp
            val passwordSignUp: EditText = binding.passwordSignUp
            val professionSignUp: EditText = binding.professionSignUp

            val nickText: String = nickSignUp.text.toString()
            val passwordText: String = passwordSignUp.text.toString()
            val professionText: String = professionSignUp.text.toString()

            if (nickText.isEmpty() || passwordText.isEmpty() || professionText.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val user = User(nickText, nickText, professionText)
                viewModel.signUpUser(user, passwordText)
            }
        }
    }

    private fun setUpProfessionEtListener() {
        val maxLength = 100

        binding.professionSignUp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length >= maxLength) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Maximum number of symbols should be less than $maxLength",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}
