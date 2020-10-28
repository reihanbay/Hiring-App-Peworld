package com.arkademy.peworld.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.ActivityProfileWorkerBinding
import com.arkademy.peworld.utils.api.ApiClient
import com.arkademy.peworld.utils.api.service.WorkerService
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class ProfileWorkerActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileWorkerBinding
    private lateinit var pagerAdapter : ProfileWorkerAdapter
    private lateinit var viewModel: ProfileWorkerViewModel
    private lateinit var coroutineScope: CoroutineScope
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_worker)
        val service = ApiClient.getApiClientToken(this)?.create(WorkerService::class.java)

        viewModel = ViewModelProvider(this).get(ProfileWorkerViewModel::class.java)
        if (service != null) {
            viewModel.setService(service)
        }
        val id = intent.getStringExtra("KEY_ID_WORKER")!!
        viewModel.getWorkerId(id.toInt())
        subscibeLiveData()

        pagerAdapter = ProfileWorkerAdapter(supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

    }

    private fun subscibeLiveData() {
        viewModel.workerLiveData.observe(this, Observer {
            Glide.with(this).load("http://34.229.16.81:8080/uploads/${it.data.image}").placeholder(R.drawable.ic_user).into(binding.photoProfile)
            binding.tvNameProfile.text = it.data.name
            binding.tvRoleJob.text = it.data.title
            binding.tvLocation.text = it.data.city
            binding.tvStatusJob.text = it.data.statusJob
            binding.tvSummary.text = it.data.description
            binding.tvSkill.text = it.data.skill
            Log.d("Check", it.data.skill)

        })
    }

    override fun onDestroy() {
        coroutineScope = CoroutineScope(Job() + Dispatchers.IO)
        coroutineScope.cancel()
        super.onDestroy()
    }
}