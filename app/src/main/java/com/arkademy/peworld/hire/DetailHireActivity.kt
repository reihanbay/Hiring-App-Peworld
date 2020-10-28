package com.arkademy.peworld.hire

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.ActivityDetailHireBinding
import com.arkademy.peworld.utils.api.ApiClient
import com.arkademy.peworld.utils.api.service.HireService
import com.arkademy.peworld.utils.sharedpreference.PreferenceHelper
import com.bumptech.glide.Glide

class DetailHireActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailHireBinding
    private lateinit var viewModel: DetailHireViewModel
    private lateinit var sharedPref: PreferenceHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_hire)
        sharedPref = PreferenceHelper(this)
        viewModel = ViewModelProvider(this).get(DetailHireViewModel::class.java)
        val service = ApiClient.getApiClientToken(this)?.create(HireService::class.java)
        if (service != null) {
            viewModel.setService(service)
        }
        val id = intent.getStringExtra("KEY_ID_HIRE")
        viewModel.getHireDetail(id.toInt())
        subscribeLiveData()

        binding.btnDecline.setOnClickListener {
            viewModel.confirmHire(id.toInt(),"rejected")
        }

        binding.btnSubmit.setOnClickListener {
            viewModel.confirmHire(id.toInt(),"accepted")
        }
    }

    private fun subscribeLiveData(){
        viewModel.getHireLiveData.observe(this, Observer {
            Glide.with(this).load("http://34.229.16.81:8008/uploads/${it.data.image}")
                .placeholder(R.drawable.ic_company).into(binding.photoProfile)
            binding.tvNameCompany.text = it.data.recruiter
            binding.tvTitleProject.text = it.data.nameProject
            binding.tvValueSalary.text = "Rp. ${it.data.price.toString()}"
            binding.tvValueDeadline.text = it.data.deadline
            binding.tvValueTodo.text = it.data.projectJob
            binding.tvMessage.text = it.data.message

            when (it.data.statusConfirm) {
                "delayed" -> {
                    binding.btnSubmit.visibility = View.VISIBLE
                    binding.btnDecline.visibility = View.VISIBLE
                    binding.tvStatus.text = it.data.statusConfirm.capitalize()
                    binding.tvStatus.backgroundTintList =
                        ContextCompat.getColorStateList(binding.tvStatus.context, R.color.gray_2)
                }
                "accepted" -> {
                    binding.btnSubmit.visibility = View.GONE
                    binding.btnDecline.visibility = View.GONE
                    binding.tvStatus.text = it.data.statusConfirm.capitalize()
                    binding.tvStatus.backgroundTintList =
                        ContextCompat.getColorStateList(binding.tvStatus.context, R.color.green_1)
                }
                "rejected" -> {
                    binding.btnSubmit.visibility = View.GONE
                    binding.btnDecline.visibility = View.GONE
                    binding.tvStatus.text = it.data.statusConfirm.capitalize()
                    binding.tvStatus.backgroundTintList =
                        ContextCompat.getColorStateList(binding.tvStatus.context, R.color.red_1)
                }
            }
        })

        viewModel.patchHireLiveData.observe(this, Observer {
            Toast.makeText(this, it.data?.statusConfirm, Toast.LENGTH_SHORT).show()
        })

        viewModel.isSuccessLiveData.observe(this, Observer {
        })
    }
}