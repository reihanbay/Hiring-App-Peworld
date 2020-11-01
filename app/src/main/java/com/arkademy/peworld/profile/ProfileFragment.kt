package com.arkademy.peworld.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.FragmentProfileBinding
import com.arkademy.peworld.login.LoginActivity
import com.arkademy.peworld.utils.api.ApiClient
import com.arkademy.peworld.utils.api.service.SkillService
import com.arkademy.peworld.utils.api.service.WorkerService
import com.arkademy.peworld.utils.sharedpreference.Constants
import com.arkademy.peworld.utils.sharedpreference.PreferenceHelper
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private lateinit var pagerAdapter : ProfileWorkerAdapter
    private lateinit var viewModel: ProfileViewModel
    private lateinit var sharedPref : PreferenceHelper
    private lateinit var recyclerView: SkillAdapter

    companion object {
        const val IMAGE_PICK_CODE = 1002
        const val PERMISSION_CODE = 1003
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        sharedPref = PreferenceHelper(activity as AppCompatActivity)

        val toolbar = binding.toolbarMain
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val supportActionBar = (activity as AppCompatActivity).supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setTitle("Profile")

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        val service = ApiClient.getApiClientToken(activity as AppCompatActivity)?.create(WorkerService::class.java)
        if (service != null) {
            viewModel.setService(service)
        }

        val serviceSkill = ApiClient.getApiClientToken(activity as AppCompatActivity)?.create(SkillService::class.java)
        if (serviceSkill != null) {
            viewModel.setServiceSkill(serviceSkill)
        }

        val id = sharedPref.getString(Constants.KEY_ID_USER).toString().toInt()
        viewModel.getWorkerId(id)
        viewModel.getSkill(id)
        subscribeLiveData()

        binding.btnEdit.setOnClickListener {
            viewModel.getSkill(id)
            setRecyclerSkill("EDITABLE")
            changeLayoutEdit()

        }
        binding.btnSaveSkill.setOnClickListener {
            viewModel.postSkill(id, binding.etSkill.text.toString())
            viewModel.getSkill(id)
            setRecyclerSkill("EDITABLE")
        }
        binding.btnCancel.setOnClickListener {
            viewModel.getSkill(id)
            setRecyclerSkill("VIEW")
            changeLayoutProfile()
        }

        binding.rlLogout.setOnClickListener{
            showDialogLogout()
        }

        setRecyclerSkill("VIEW")
        adapter()
        return binding.root
    }
    private fun subscribeLiveData() {
        viewModel.workerLiveData.observe(this, Observer {
            Glide.with(this).load("http://34.229.16.81:8008/uploads/${it.data.image}").placeholder(R.drawable.ic_user).into(binding.photoProfile)
            binding.tvNameProfile.text = it.data.name
            binding.tvRoleJob.text = it.data.title
            binding.tvLocation.text = it.data.city
            binding.tvStatusJob.text = it.data.statusJob
            binding.tvSummary.text = it.data.description
            binding.etFullname.setText(it.data.name)
            binding.etRole.setText(it.data.title)
            binding.etJobStatus.setText(it.data.statusJob)
            binding.etDomisili.setText(it.data.city)
            binding.etWorkPlace.setText(it.data.workPlace)
            binding.etSummary.setText(it.data.description)
        })
        viewModel.toastUpdateLiveData.observe(this, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.isProgressLiveData.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.llEdit.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(context!!,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permission, PERMISSION_CODE)
                } else {
                    pickImageFromGallery()
                }
            } else {
                pickImageFromGallery()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        sharedPref = PreferenceHelper(activity as AppCompatActivity)
        val id = sharedPref.getString(Constants.KEY_ID_USER).toString().toInt()
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        val service = ApiClient.getApiClientToken(activity as AppCompatActivity)?.create(WorkerService::class.java)
        if (service != null) {
            viewModel.setService(service)
        }

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            Toast.makeText(activity as AppCompatActivity, data?.data.toString(), Toast.LENGTH_SHORT).show()

            binding.editPhotoProfile.setImageURI(data?.data)
            binding.editPhotoProfile.clipToOutline = true
            val filePath = getPath(activity as AppCompatActivity, data?.data)
            val file = File(filePath)

            var img: MultipartBody.Part? = null
            val mediaTypeImg = "image/png".toMediaType()
            val inputStream = context?.contentResolver?.openInputStream(data?.data!!)
            val reqFile: RequestBody? = inputStream?.readBytes()?.toRequestBody(mediaTypeImg)

            img = reqFile?.let { it ->
                MultipartBody.Part.createFormData(
                        "image", file.name,
                        it
                )
            }
            binding.btnSave.setOnClickListener {
                val nameWorker = createPartFromString(binding.etFullname.text.toString())
                val jobTitle = createPartFromString(binding.etRole.text.toString())
                val statusJob = createPartFromString(binding.etJobStatus.text.toString())
                val city = createPartFromString(binding.etDomisili.text.toString())
                val workPlace = createPartFromString(binding.etWorkPlace.text.toString())
                val description = createPartFromString(binding.etSummary.text.toString())
                viewModel.patchWorkerId(id,nameWorker, jobTitle, statusJob, city, workPlace, description, img )
                changeLayoutProfile()

            }
            viewModel.getWorkerId(id)
            setRecyclerSkill("VIEW")
            adapter()

        }


    }

    private fun adapter(){
        pagerAdapter = ProfileWorkerAdapter((activity as AppCompatActivity).supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    private fun setRecyclerSkill(code: String){
        recyclerView = SkillAdapter(code, arrayListOf(), object : SkillAdapter.OnClickViewListener {
            override fun OnClick(id: Int?) {
                viewModel.deleteSkill(id)
                viewModel.getSkill(id)
            }
        })

        binding.rvSkill.adapter = recyclerView
        binding.rvSkill.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL)
    }


    private fun changeLayoutEdit(){
        binding.llTab.visibility = View.GONE
        binding.tvSummary.visibility = View.GONE
        binding.btnEdit.visibility = View.GONE
        binding.tvMail.visibility = View.GONE
        binding.tvGithub.visibility = View.GONE
        binding.tvGitlab.visibility = View.GONE
        binding.tvInstagram.visibility = View.GONE
        binding.ivIcMail.visibility = View.GONE
        binding.ivIcInstagram.visibility = View.GONE
        binding.ivIcGitlab.visibility = View.GONE
        binding.ivIcGithub.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.photoProfile.visibility = View.GONE
        binding.rvSkill.visibility = View.VISIBLE
        binding.rlLogout.visibility = View.GONE

        binding.clEditData.visibility = View.VISIBLE
        binding.clSkillData.visibility = View.VISIBLE
        binding.btnSave.visibility = View.VISIBLE
        binding.btnCancel.visibility = View.VISIBLE
        binding.llEdit.visibility = View.VISIBLE

        binding.photoProfile.clipToOutline


    }
    private fun changeLayoutProfile(){
        binding.llTab.visibility = View.VISIBLE
        binding.tvSummary.visibility = View.VISIBLE
        binding.btnEdit.visibility = View.VISIBLE
        binding.tvSkillHeader.visibility =View.VISIBLE
        binding.tvMail.visibility = View.VISIBLE
        binding.tvGithub.visibility = View.VISIBLE
        binding.tvGitlab.visibility = View.VISIBLE
        binding.tvInstagram.visibility = View.VISIBLE
        binding.ivIcMail.visibility = View.VISIBLE
        binding.ivIcInstagram.visibility = View.VISIBLE
        binding.ivIcGitlab.visibility = View.VISIBLE
        binding.ivIcGithub.visibility = View.VISIBLE
        binding.photoProfile.visibility = View.VISIBLE


        binding.rlLogout.visibility = View.VISIBLE
        binding.clSkillData.visibility = View.GONE
        binding.clEditData.visibility = View.GONE
        binding.btnSave.visibility = View.GONE
        binding.btnCancel.visibility = View.GONE
        binding.llEdit.visibility = View.GONE
    }
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(activity as AppCompatActivity, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun getPath(context: Context, uri: Uri?): String {
        var result: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? =
                uri?.let { context.contentResolver.query(it, proj, null, null, null) }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(proj[0])
                result = cursor.getString(column_index)
            }
            cursor.close()
        }
        if (result == null) {
            result = "Not found"
        }
        return result
    }

    @NonNull
    private fun createPartFromString(json: String): RequestBody {
        val mediaType = "multipart/form-data".toMediaType()
        return json
                .toRequestBody(mediaType)
    }

    private fun showDialogLogout() {
        val dialog = AlertDialog.Builder(activity as AppCompatActivity)
                .setTitle("Logout")
                .setMessage("Are You Sure?")
                .setPositiveButton("Logout") { dialog: DialogInterface?, which: Int ->
                    sharedPref.clear()
                    startActivity(Intent(activity as AppCompatActivity, LoginActivity::class.java))
                }
                .setNegativeButton("Back") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
        dialog.show()
    }
}