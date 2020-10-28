package com.arkademy.peworld.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.os.postDelayed
import androidx.databinding.DataBindingUtil
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.ActivitySplashBinding
import com.arkademy.peworld.onboarding.OnBoardActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        Handler().postDelayed({
            val intent = Intent(this, OnBoardActivity::class.java)
            startActivity(intent)
            finish()
        }, 4000)
    }
}