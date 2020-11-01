package com.arkademy.peworld.profile.form

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.ActivityFormExperienceBinding
import com.arkademy.peworld.utils.api.ApiClient
import com.arkademy.peworld.utils.api.service.ExperienceService
import com.arkademy.peworld.utils.model.ExperienceModel
import com.arkademy.peworld.utils.sharedpreference.Constants
import com.arkademy.peworld.utils.sharedpreference.PreferenceHelper
import kotlinx.coroutines.*

class FormExperienceActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFormExperienceBinding
    private lateinit var viewModel: FormExperienceViewModel
    private lateinit var sharedPref : PreferenceHelper
    private lateinit var coroutineScope: CoroutineScope

    companion object {
        const val CODE_CONFIRM = 2001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_form_experience)
        sharedPref = PreferenceHelper(this)
        viewModel = ViewModelProvider(this).get(FormExperienceViewModel::class.java)

        val toolbar = binding.toolbarMain
        this.setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val service = ApiClient.getApiClientToken(this)?.create(ExperienceService::class.java)
        if (service != null) {
            viewModel.setService(service)
        }
        val idWorker = sharedPref.getString(Constants.KEY_ID_USER)
        val setData = intent.getParcelableExtra<ExperienceModel>("DATA_PARCELIZE")
        val codeLayout = intent.getStringExtra("CODE_LAYOUT")

        val codeState = intent.getStringExtra("CODE_EXPERIENCE")



        if (codeState == "ADD") {
            binding.btnSubmit.visibility = View.VISIBLE
            binding.btnEdit.visibility = View.GONE
            binding.btnCancel.visibility = View.GONE
            binding.tvEdit.visibility = View.GONE

            binding.btnSubmit.setOnClickListener {
                val nameCompany = binding.etCompanyName.text.toString()
                val position = binding.etWorkPosition.text.toString()
                val start = binding.etWorkJoin.text.toString()
                val end = binding.etWorkOut.text.toString()
                val desc = binding.etDescription.text.toString()
                viewModel.postExperience(nameCompany,desc,position,start,end, idWorker.toString().toInt())
                Log.d(
                    "check Post", "$nameCompany $desc $position $start $end  ${
                        idWorker.toString().toInt()
                    }"
                )
                setResult(Activity.RESULT_OK)
                finish()
            }
        } else if (codeState == "DETAIL") {
            if (codeLayout == "WORKER") {
                binding.tvEdit.visibility = View.GONE
                binding.btnSubmit.visibility = View.GONE
                binding.etCompanyName.setText(setData?.companyName)
                binding.etWorkPosition.setText(setData?.workPosition)
                binding.etWorkJoin.setText(setData?.start)
                binding.etWorkOut.setText(setData?.end)
                binding.etDescription.setText(setData?.description)
                binding.etCompanyName.isEnabled = false
                binding.etWorkPosition.isEnabled = false
                binding.etWorkJoin.isEnabled = false
                binding.etWorkOut.isEnabled = false
                binding.etDescription.isEnabled = false
            } else {
                val nameCompany = binding.etCompanyName.text.toString()
                val position = binding.etWorkPosition.text.toString()
                val start = binding.etWorkJoin.text.toString()
                val end = binding.etWorkOut.text.toString()
                val desc = binding.etDescription.text.toString()
                binding.tvEdit.visibility = View.VISIBLE

                binding.btnSubmit.visibility = View.GONE
                binding.etCompanyName.setText(setData?.companyName)
                binding.etWorkPosition.setText(setData?.workPosition)
                binding.etWorkJoin.setText(setData?.start)
                binding.etWorkOut.setText(setData?.end)
                binding.etDescription.setText(setData?.description)

                binding.tvEdit.setOnClickListener {
                    binding.btnDelete.visibility = View.VISIBLE
                    binding.btnEdit.visibility = View.VISIBLE
                    binding.btnCancel.visibility = View.VISIBLE
                    binding.tvEdit.visibility = View.GONE
                }
                binding.btnCancel.setOnClickListener {
                    binding.btnDelete.visibility = View.GONE
                    binding.btnEdit.visibility = View.GONE
                    binding.btnCancel.visibility = View.GONE
                    binding.tvEdit.visibility = View.VISIBLE

                }

                binding.btnEdit.setOnClickListener {
                    viewModel.patchExperience(setData.idExperience,nameCompany,desc,position,start,end, idWorker.toString().toInt())
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                binding.btnDelete.setOnClickListener {
                    viewModel.deleteExperience(setData.idExperience)
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }

        subscribeLiveData()
    }

    private fun subscribeLiveData(){
        viewModel.isSuccessLiveData.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope = CoroutineScope(Job() + Dispatchers.IO)
        coroutineScope.cancel()
    }
}