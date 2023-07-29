package ge.mjavarchik.messenger.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import ge.mjavarchik.messenger.R
import ge.mjavarchik.messenger.adapters.ViewPagerAdapter
import ge.mjavarchik.messenger.databinding.MainPagesGeneralBinding
import ge.mjavarchik.messenger.fragments.ProfileSettingsFragment
import ge.mjavarchik.messenger.fragments.UserHomePageFragment

class UserPagesActivity : AppCompatActivity() {
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var bottomAppBar: BottomAppBar
    private lateinit var viewPager: ViewPager2
    private var fragments = arrayListOf(UserHomePageFragment(), ProfileSettingsFragment())
    private lateinit var binding: MainPagesGeneralBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainPagesGeneralBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //curr user
        bottomAppBar = binding.bottomAppBar
        viewPager = binding.viewPager2

        viewPager.adapter = ViewPagerAdapter(this, fragments)
        val navigationView = binding.bottomNavigationView
        bottomNavView = binding.bottomNavigationView
        bottomNavView.background = null


        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                when (position){
                    0 -> navigationView.selectedItemId = R.id.menu_main_page
                    1 -> navigationView.selectedItemId = R.id.menu_profile
                }

            }
        })

        navigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_main_page -> viewPager.currentItem= 0
                R.id.menu_profile -> viewPager.currentItem= 1
            }
            true
        }
    }
}