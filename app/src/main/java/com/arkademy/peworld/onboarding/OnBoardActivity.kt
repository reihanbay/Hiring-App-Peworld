package com.arkademy.peworld.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.arkademy.peworld.MainActivity
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.ActivityOnBoardBinding
import com.arkademy.peworld.login.LoginActivity
import com.arkademy.peworld.register.RegisterActivity
import com.arkademy.peworld.utils.sharedpreference.Constants
import com.arkademy.peworld.utils.sharedpreference.PreferenceHelper

class OnBoardActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityOnBoardBinding
    private lateinit var sharedPref : PreferenceHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_board)

        binding.btnSignIn.setOnClickListener(this)
        binding.btnSignUp.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        sharedPref = PreferenceHelper(this)
        if (sharedPref.getBoolean(Constants.LOGIN)!!){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
    override fun onClick(v: View?) {
        binding.apply {
            when(v){
                btnSignIn -> startActivity(Intent(this@OnBoardActivity, LoginActivity::class.java))
                btnSignUp -> startActivity(Intent(this@OnBoardActivity, RegisterActivity::class.java))
            }
        }
    }
}