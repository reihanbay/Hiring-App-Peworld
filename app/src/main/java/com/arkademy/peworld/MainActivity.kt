package com.arkademy.peworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.arkademy.peworld.databinding.ActivityMainBinding
import com.arkademy.peworld.hire.HireFragment
import com.arkademy.peworld.home.HomeFragment
import com.arkademy.peworld.profile.ProfileFragment
import com.arkademy.peworld.search.SearchFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        val home = HomeFragment()
        val search = SearchFragment()
        val hire = HireFragment()
        val profile = ProfileFragment()

        currentFragment(home)
        binding.bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_homeFragment -> currentFragment(home)
                R.id.ic_searchFragment -> currentFragment(search)
                R.id.ic_jobFragment -> currentFragment(hire)
                R.id.ic_profileFragment -> currentFragment(profile)
                else -> false
            }

        }
    }

    fun currentFragment(fragment: Fragment) : Boolean{
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
                .commit()
        }
        return true
    }
}