package ge.mjavarchik.messenger.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ge.mjavarchik.messenger.databinding.LogInPageBinding

class LogInActivity : AppCompatActivity() {

    private lateinit var binding: LogInPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LogInPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun signUP(view: View) {
        startActivity(Intent(this, SignUpActivity::class.java))
    }
}