package com.arkademy.peworld.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.ActivityProfileWorkerBinding
import com.arkademy.peworld.databinding.FragmentProfileBinding
import com.arkademy.peworld.utils.api.ApiClient
import com.arkademy.peworld.utils.api.service.WorkerService
import com.arkademy.peworld.utils.sharedpreference.Constants
import com.arkademy.peworld.utils.sharedpreference.PreferenceHelper
import com.bumptech.glide.Glide

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private lateinit var pagerAdapter : ProfileWorkerAdapter
    private lateinit var viewModel: ProfileViewModel
    private lateinit var sharedPref : PreferenceHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        val service = ApiClient.getApiClientToken(activity as AppCompatActivity)?.create(WorkerService::class.java)
        sharedPref = PreferenceHelper(activity as AppCompatActivity)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        if (service != null) {
            viewModel.setService(service)
        }
        val id = sharedPref.getString(Constants.KEY_ID_USER)
        viewModel.getWorkerId(3)
        subscibeLiveData()

        pagerAdapter = ProfileWorkerAdapter((activity as AppCompatActivity).supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        return binding.root
    }

    private fun subscibeLiveData() {
        viewModel.workerLiveData.observe(this, Observer {
            Glide.with(this).load("http://34.229.16.81:8008/uploads/${it.data.image}").placeholder(R.drawable.ic_user).into(binding.photoProfile)
            binding.tvNameProfile.text = it.data.name
            binding.tvRoleJob.text = it.data.title
            binding.tvLocation.text = it.data.city
            binding.tvStatusJob.text = it.data.statusJob
            binding.tvSummary.text = it.data.description
            binding.tvSkill.text = it.data.skill
        })
    }

}