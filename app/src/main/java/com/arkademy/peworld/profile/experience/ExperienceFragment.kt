package com.arkademy.peworld.profile.experience

import android.app.Activity
import android.content.Intent
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
import com.arkademy.peworld.profile.form.FormExperienceActivity
import com.arkademy.peworld.utils.api.ApiClient
import com.arkademy.peworld.utils.api.service.ExperienceService
import com.arkademy.peworld.utils.model.ExperienceModel
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
            viewModel.getExperience(id.toInt())
        } else {
            val idUser = sharedPref.getString(Constants.KEY_ID_USER)
            if (idUser != null) {
                viewModel.getExperience(idUser.toString().toInt())
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == FormExperienceActivity.CODE_CONFIRM){
            val service = ApiClient.getApiClientToken(activity as AppCompatActivity)?.create(ExperienceService::class.java)
            if (service != null) {
                viewModel.setServiceExperience(service)
            }

            val id = (activity as AppCompatActivity).intent.getStringExtra("KEY_ID_WORKER")

            if(id != null) {
                viewModel.getExperience(id.toInt())
            } else {
                val idUser = sharedPref.getString(Constants.KEY_ID_USER)
                if (idUser != null) {
                    viewModel.getExperience(idUser.toString().toInt())
                }
            }
            subsribceLiveData()
            setRecyclerView()
        }
    }
    private fun setRecyclerView(){
        val id = (activity as AppCompatActivity).intent.getStringExtra("KEY_ID_WORKER")
        sharedPref = PreferenceHelper(activity as AppCompatActivity)
        val idUser = sharedPref.getString(Constants.KEY_ID_USER)
        var code : String? = null
        if (id != null) {
            code = "WORKER"
        } else {
            if(idUser != null) {
                code = "USER"
            } else {
                code = null
            }
        }
        recyclerView = ExperienceAdapter(code, arrayListOf(), object : ExperienceAdapter.OnClickViewListener{
            override fun OnClick(data: ExperienceModel?) {
                if (data != null) {
                    val intent = Intent(activity, FormExperienceActivity::class.java)
                    intent.putExtra("CODE_EXPERIENCE", "DETAIL")
                    intent.putExtra("DATA_PARCELIZE", data)
                    intent.putExtra("CODE_LAYOUT", code)
                    startActivityForResult(intent, FormExperienceActivity.CODE_CONFIRM)
                }
            }

            override fun OnClickAdd() {
                val intent = Intent(activity, FormExperienceActivity::class.java)
                intent.putExtra("CODE_EXPERIENCE", "ADD")
                startActivityForResult(intent, FormExperienceActivity.CODE_CONFIRM)
            }

        })
        binding.rvExperience.adapter = recyclerView
        binding.rvExperience.layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL, false)
    }

}