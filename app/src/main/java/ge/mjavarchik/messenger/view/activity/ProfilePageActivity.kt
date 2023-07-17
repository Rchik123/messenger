package ge.mjavarchik.messenger.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ge.mjavarchik.messenger.R
import ge.mjavarchik.messenger.databinding.ActivityProfilePageBinding

class ProfilePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val username = intent.getStringExtra("username")
        val profession = intent.getStringExtra("profession")

        // Display the username and email in the UI
        binding.etUsername.setText(username)
        binding.etProfession.setText(profession)
    }
}