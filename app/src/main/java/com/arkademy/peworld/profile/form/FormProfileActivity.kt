package com.arkademy.peworld.profile.form

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arkademy.peworld.MainActivity
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.ActivityFormProfileBinding
import com.arkademy.peworld.login.LoginActivity
import com.arkademy.peworld.utils.api.ApiClient
import com.arkademy.peworld.utils.api.service.WorkerService
import com.arkademy.peworld.utils.sharedpreference.Constants
import com.arkademy.peworld.utils.sharedpreference.PreferenceHelper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class FormProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFormProfileBinding
    private lateinit var viewModel: FormProfileViewModel
    private lateinit var sharedPref: PreferenceHelper


    companion object {
        const val IMAGE_PICK_CODE = 1000
        const val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,  R.layout.activity_form_profile)
        viewModel = ViewModelProvider(this).get(FormProfileViewModel::class.java)
        val service = ApiClient.getApiClientToken(this)?.create(WorkerService::class.java)
        if (service != null) {
            viewModel.setService(service)
        }

        binding.photoProfile.clipToOutline
        binding.btnUpload.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
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
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
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
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        sharedPref = PreferenceHelper(this)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            binding.photoProfile.setImageURI(data?.data)
            binding.photoProfile.clipToOutline = true
            val filePath = getPath(this, data?.data)
            val file = File(filePath)

            var img: MultipartBody.Part? = null
            val mediaTypeImg = "image/png".toMediaType()
            val inputStream = contentResolver.openInputStream(data?.data!!)
            val reqFile: RequestBody? = inputStream?.readBytes()?.toRequestBody(mediaTypeImg)

            img = reqFile?.let { it ->
                MultipartBody.Part.createFormData(
                        "image", file.name,
                        it
                )
            }
            subscribeLiveData()
            binding.btnSubmit.setOnClickListener {
                val nameWorker = createPartFromString(binding.etFullname.text.toString())
                val role = createPartFromString(binding.etRole.text.toString())
                val jobStatus = createPartFromString(binding.etJobStatus.text.toString())
                val domicile = createPartFromString(binding.etDomisili.text.toString())
                val workePlace = createPartFromString(binding.etWorkPlace.text.toString())
                val description = createPartFromString(binding.etSummary.text.toString())
                val idAccount =
                        createPartFromString(sharedPref.getInt(Constants.KEY_ID_ACCOUNT).toString())
                if (img != null) {
                    viewModel.postDataProfile(nameWorker, role, jobStatus, domicile, workePlace,description, idAccount, img)
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

    fun subscribeLiveData(){
        viewModel.isToastLiveData.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.isSuccessLiveData.observe(this, Observer {
            if (it) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }
}