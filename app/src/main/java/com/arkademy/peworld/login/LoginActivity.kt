package com.arkademy.peworld.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arkademy.peworld.MainActivity
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.ActivityLoginBinding
import com.arkademy.peworld.profile.form.FormProfileActivity
import com.arkademy.peworld.register.RegisterActivity
import com.arkademy.peworld.reset.ResetPasswordActivity
import com.arkademy.peworld.utils.api.ApiClient
import com.arkademy.peworld.utils.api.response.loginResponse
import com.arkademy.peworld.utils.api.service.AccountService
import com.arkademy.peworld.utils.api.service.WorkerService
import com.arkademy.peworld.utils.sharedpreference.Constants
import com.arkademy.peworld.utils.sharedpreference.PreferenceHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var sharedPref : PreferenceHelper
    private lateinit var coroutineScope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        sharedPref = PreferenceHelper(this)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        val service = ApiClient.getApiClientToken(this)?.create(AccountService::class.java)
        val serviceCheck = ApiClient.getApiClientToken(this)?.create(WorkerService::class.java)
        if (serviceCheck != null) {
            viewModel.setServiceCheck(serviceCheck)
        }

        if (service != null) {
            viewModel.setService(service)
        }
        subsribeLiveData()

        binding.btnLogin.setOnClickListener(this)
        binding.tvResetPassword.setOnClickListener(this)
        binding.tvToRegister.setOnClickListener(this)


    }

    override fun onDestroy() {
        coroutineScope = CoroutineScope(Job() + Dispatchers.IO)
        coroutineScope.cancel()
        super.onDestroy()
    }

    private fun checkLiveData(){
        viewModel.checkLiveData.observe(this, Observer {
            if(it.data == null) {
                val intent = Intent(this, FormProfileActivity::class.java)
                startActivity(intent)
            } else {
                sharedPref = PreferenceHelper(this)
                sharedPref.putString(Constants.KEY_ID_USER, it.data[0].idWorker.toString())
                Log.d("checkId", it.data[0].idWorker.toString())
            }
        })
    }
    private fun subsribeLiveData(){

        viewModel.isSuccessLiveData.observe(this, Observer {
            if (it) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }
        })
        viewModel.isToastLiveData.observe(this, Observer {
            if (!it) {
                Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.isProgressLiveData.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.VISIBLE
            }
        })
        viewModel.responseLiveData.observe(this, Observer {
            sharedPref = PreferenceHelper(this)
            sharedPref.putInt(Constants.KEY_ID_ACCOUNT, it.data.id.toString().toInt())
            sharedPref.putString(Constants.KEY_TOKEN, it.data.token.toString())
            sharedPref.putString(Constants.USERNAME, it.data.name)
            sharedPref.putBoolean(Constants.LOGIN, true)
            viewModel.checkDataApi(it.data.id.toString().toInt())
            checkLiveData()
        })
    }

    override fun onClick(v: View?) {
        binding.apply {
            when(v){
                btnLogin -> {
                    val email = binding.etEmail.text.toString()
                    val password = binding.etPassword.text.toString()
                    if (email.isNotEmpty()){
                        if(password.isNotEmpty()){
                            viewModel.loginService(
                                email, password
                            )
                        } else {
                            Toast.makeText(this@LoginActivity, "Fill the Password ", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Fill the Email First", Toast.LENGTH_SHORT).show()
                    }
                }
                tvResetPassword -> {startActivity(Intent(this@LoginActivity, ResetPasswordActivity::class.java))}
                tvToRegister -> {startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))}
            }
        }
    }
}