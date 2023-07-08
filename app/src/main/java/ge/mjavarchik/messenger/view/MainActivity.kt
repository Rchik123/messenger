package ge.mjavarchik.messenger.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ge.mjavarchik.messenger.R
import ge.mjavarchik.messenger.databinding.ActivityMainBinding
import ge.mjavarchik.messenger.databinding.LogInPageBinding

class MainActivity : AppCompatActivity() {

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