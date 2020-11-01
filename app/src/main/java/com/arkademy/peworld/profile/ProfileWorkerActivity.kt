package com.arkademy.peworld.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.ActivityProfileWorkerBinding
import com.arkademy.peworld.utils.api.ApiClient
import com.arkademy.peworld.utils.api.service.SkillService
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
    private lateinit var recyclerView : SkillAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_worker)

        val toolbar = binding.toolbarMain
        this.setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }


        val service = ApiClient.getApiClientToken(this)?.create(WorkerService::class.java)
        val serviceSkill = ApiClient.getApiClientToken(this)?.create(SkillService::class.java)

        viewModel = ViewModelProvider(this).get(ProfileWorkerViewModel::class.java)
        if (service != null) {
            viewModel.setService(service)
        }
        if (serviceSkill != null) {
            viewModel.setServiceSkill(serviceSkill)
        }
        val id = intent.getStringExtra("KEY_ID_WORKER")!!
        viewModel.getSkill(id.toInt())
        viewModel.getWorkerId(id.toInt())
        subscibeLiveData()
        setRecyclerSkill()
        pagerAdapter = ProfileWorkerAdapter(supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

    }

    private fun subscibeLiveData() {
        viewModel.workerLiveData.observe(this, Observer {
            Glide.with(this).load("http://34.229.16.81:8008/uploads/${it.data.image}").placeholder(R.drawable.ic_user).into(binding.photoProfile)
            binding.tvNameProfile.text = it.data.name
            binding.tvRoleJob.text = it.data.title
            binding.tvLocation.text = it.data.city
            binding.tvStatusJob.text = it.data.statusJob
            binding.tvSummary.text = it.data.description
        })
        viewModel.listSkillLiveData.observe(this, Observer {
            (binding.rvSkill.adapter as SkillAdapter).addList(it)
        })
        viewModel.isSuccessSkillLiveData.observe(this, Observer {
            if (it) {
                binding.tvSkill.visibility = View.GONE
                binding.rvSkill.visibility = View.VISIBLE
            } else {
                binding.rvSkill.visibility = View.GONE
                binding.tvSkill.visibility = View.VISIBLE
            }
        })
    }

    private fun setRecyclerSkill(){
        recyclerView = SkillAdapter("VIEW", arrayListOf(), object : SkillAdapter.OnClickViewListener {
            override fun OnClick(id: Int?) {
            }
        })

        binding.rvSkill.adapter = recyclerView
        binding.rvSkill.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL)
    }

    override fun onDestroy() {
        coroutineScope = CoroutineScope(Job() + Dispatchers.IO)
        coroutineScope.cancel()
        super.onDestroy()
    }
}