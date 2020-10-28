package com.arkademy.peworld.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.ActivityRegisterBinding
import com.arkademy.peworld.login.LoginActivity
import com.arkademy.peworld.utils.api.ApiClient
import com.arkademy.peworld.utils.api.service.AccountService
import com.arkademy.peworld.utils.sharedpreference.PreferenceHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private lateinit var sharedPref: PreferenceHelper
    private lateinit var coroutineScope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        sharedPref = PreferenceHelper(this)
        val service = ApiClient.getApiClientToken(this)?.create(AccountService::class.java)
        if (service != null) {
            viewModel.setService(service)
        }
        subscribeLiveData()
        binding.btnRegister.setOnClickListener(this)
        binding.tvToLogin.setOnClickListener(this)
    }

    private fun subscribeLiveData() {
        viewModel.isProgressLiveData.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
        viewModel.toastLiveData.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
        viewModel.isSuccessLiveData.observe(this, Observer {
            if (it){
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        })
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v) {
                btnRegister -> {
                    viewModel.postRegister(binding.etName.text.toString(), binding.etEmail.text.toString(), binding.etPassword.text.toString(), binding.etPhone.text.toString(), 0)
                }
                tvToLogin -> startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }
    }

    override fun onDestroy() {
        coroutineScope = CoroutineScope(Job() + Dispatchers.IO)
        coroutineScope.cancel()
        super.onDestroy()
    }
}