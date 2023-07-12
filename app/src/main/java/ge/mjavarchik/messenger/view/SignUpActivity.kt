package ge.mjavarchik.messenger.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
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

        viewModel.signedUp.observe(this) {
            Toast.makeText(this, "SIGNED UP = true !!!", Toast.LENGTH_SHORT).show()
        }

        val signUpButton: Button = binding.signUpButton
        signUpButton.setOnClickListener {
            val nickSignUp: EditText = binding.nickSignUp
            val passwordSignUp: EditText = binding.passwordSignUp
            val professionSignUp: EditText = binding.professionSignUp

            val nickText: String = nickSignUp.text.toString()
            val passwordText: String = passwordSignUp.text.toString()
            val professionText: String = professionSignUp.text.toString()

            if (nickText.isEmpty() || passwordText.isEmpty() || professionText.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val user = User(nickText, professionText, passwordText)
                registerUser(user)
            }
        }
        val editText: EditText = binding.professionSignUp
        val maxLength = 100

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length >= maxLength) {
                    Toast.makeText(this@SignUpActivity, "Maximum number of symbols should be less than $maxLength", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun registerUser(user: User) {
        viewModel.registerUser(user)
    }
}
