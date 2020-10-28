package com.arkademy.peworld.profile.experience

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.FragmentExperienceBinding
import com.arkademy.peworld.utils.api.ApiClient
import com.arkademy.peworld.utils.api.service.ExperienceService
import com.arkademy.peworld.utils.sharedpreference.Constants
import com.arkademy.peworld.utils.sharedpreference.PreferenceHelper

class ExperienceFragment : Fragment() {
    private lateinit var binding : FragmentExperienceBinding
    private lateinit var viewModel: ExperienceViewModel
    private lateinit var recyclerView: ExperienceAdapter
    private lateinit var sharedPref : PreferenceHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_experience, container, false)
        sharedPref = PreferenceHelper(activity as AppCompatActivity)
        viewModel = ViewModelProvider(this).get(ExperienceViewModel::class.java)

        val service = ApiClient.getApiClientToken(activity as AppCompatActivity)?.create(ExperienceService::class.java)
        if (service != null) {
            viewModel.setServiceExperience(service)
        }

        val id = (activity as AppCompatActivity).intent.getStringExtra("KEY_ID_WORKER")

        if(id != null) {
            Log.d("checking", id.toString())
            viewModel.getExperience(id.toInt())
        } else {
            val idUser = sharedPref.getString(Constants.KEY_ID_USER)
            if (idUser != null) {
                viewModel.getExperience(idUser.toString().toInt())
                Log.d("checking", idUser.toString())

            }
        }
        subsribceLiveData()
        setRecyclerView()
        return binding.root
    }

    private fun subsribceLiveData(){
        viewModel.experienceLiveData.observe(this, Observer {
            (binding.rvExperience.adapter as ExperienceAdapter).addList(it)
        })
        viewModel.isEmptyLiveData.observe(this, Observer {
            if(it){
                binding.llEmpty.visibility = View.VISIBLE
                binding.rvExperience.visibility = View.GONE
            }else {
                binding.llEmpty.visibility = View.GONE
                binding.rvExperience.visibility = View.VISIBLE
            }
        })
    }

    private fun setRecyclerView(){
        recyclerView = ExperienceAdapter(arrayListOf())
        binding.rvExperience.adapter = recyclerView
        binding.rvExperience.layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL, false)
    }

}