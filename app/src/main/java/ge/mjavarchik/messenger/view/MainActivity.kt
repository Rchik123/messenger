package ge.mjavarchik.messenger.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

import ge.mjavarchik.messenger.databinding.LogInPageBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: LogInPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LogInPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun signUP(view: View) {
        Firebase.analytics.logEvent("signupclicked", null)
        startActivity(Intent(this, SignUpActivity::class.java))
    }
}