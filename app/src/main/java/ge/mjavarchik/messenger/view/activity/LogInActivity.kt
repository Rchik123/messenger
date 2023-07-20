package ge.mjavarchik.messenger.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import ge.mjavarchik.messenger.databinding.LogInPageBinding
import ge.mjavarchik.messenger.viewmodel.LogInViewModel

class LogInActivity : AppCompatActivity() {

    private val viewModel: LogInViewModel by viewModels {
        LogInViewModel.getViewModelFactory(applicationContext)
    }
    private lateinit var binding: LogInPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onIfLoggedIn()

        binding = LogInPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpSignedInObserver()
        setUpSignInBtnListener()
    }

    private fun onIfLoggedIn() {
        if(viewModel.wasAlreadyLoggedIn()){
            startActivity(Intent(this, LoggedInActivity::class.java))
            finish()
        }
    }

    private fun setUpSignInBtnListener() {
        binding.signInButton.setOnClickListener {

            val nickSignIn: EditText = binding.nickLogIn
            val passwordSignIn: EditText = binding.passwordLogIn

            val nickText: String = nickSignIn.text.toString()
            val passwordText: String = passwordSignIn.text.toString()

            if (nickText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.signInUser(nickText, passwordText)
            }
        }
    }
    private fun setUpSignedInObserver() {
        viewModel.signedIn.observe(this) { signedIn ->
            if (signedIn) {
                Toast.makeText(this, "Log in successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoggedInActivity::class.java))
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Invalid UserName or Password!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun signUP(view: View) {
        startActivity(Intent(this, SignUpActivity::class.java))
    }
}