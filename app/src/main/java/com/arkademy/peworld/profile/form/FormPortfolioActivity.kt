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
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.ActivityFormPortfolioBinding
import com.arkademy.peworld.utils.api.ApiClient
import com.arkademy.peworld.utils.api.service.PortfolioService
import com.arkademy.peworld.utils.model.PortfolioModel
import com.arkademy.peworld.utils.sharedpreference.Constants
import com.arkademy.peworld.utils.sharedpreference.PreferenceHelper
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class FormPortfolioActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFormPortfolioBinding
    private lateinit var viewModel: FormPortfolioViewModel
    private lateinit var sharedPref : PreferenceHelper
    private lateinit var coroutineScope : CoroutineScope
    private var selectedRadio : String? = null

    companion object {
        const val PERMISSION_CODE = 1003
        const val IMAGE_PICK_CODE = 1004
        const val CODE_CONFIRM = 1005
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_form_portfolio)
        viewModel = ViewModelProvider(this).get(FormPortfolioViewModel::class.java)

        val toolbar = binding.toolbarMain
        this.setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val service = ApiClient.getApiClientToken(this)?.create(PortfolioService::class.java)
        if (service != null) {
            viewModel.setService(service)
        }
        setRadioSelected()
        val code = intent.getStringExtra("CODE")
        val data = intent.getParcelableExtra<PortfolioModel>("DATA_PARCELIZE")
        if (code == "EDIT") {
            binding.etAppName.setText(data.namePortfolio)
            binding.etRepoLink.setText(data.linkRepo)
            if(data.typePortfolio == "mobile") {
                binding.radioMobile.isChecked = true
            } else if (data.typePortfolio == "web") {
                binding.radioWeb.isChecked = true
            }
            Glide.with(this).load("http://34.229.16.81:8008/uploads/${data.image}")
                .placeholder(R.drawable.ic_user).into(binding.ivPortfolio)
            binding.ivPortfolio.backgroundTintList = ContextCompat.getColorStateList(this, R.color.background_gray)
            binding.ivPortfolio.clipToOutline = true
            binding.btnDelete.visibility = View.VISIBLE
            binding.btnSave.visibility = View.VISIBLE
            binding.btnSubmit.visibility = View.GONE

            binding.btnDelete.setOnClickListener {
                viewModel.deletePortfolio(data.idPortfolio)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

        binding.ivPortfolio.clipToOutline
        binding.ivPortfolio.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permission, FormProfileActivity.PERMISSION_CODE)
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
        startActivityForResult(intent,IMAGE_PICK_CODE)
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
        viewModel = ViewModelProvider(this).get(FormPortfolioViewModel::class.java)
        val dataSet = intent.getParcelableExtra<PortfolioModel>("DATA_PARCELIZE")

        val service = ApiClient.getApiClientToken(this)?.create(PortfolioService::class.java)
        if (service != null) {
            viewModel.setService(service)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            binding.ivPortfolio.visibility = View.GONE

            binding.ivEditPortfolio.visibility = View.VISIBLE
            binding.ivEditPortfolio.setImageURI(data?.data)
            binding.ivEditPortfolio.backgroundTintList = ContextCompat.getColorStateList(this, R.color.background_gray)
            binding.ivEditPortfolio.clipToOutline = true

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
                setRadioSelected()
                val id = createPartFromString(sharedPref.getString(Constants.KEY_ID_USER)!!)
                val name = createPartFromString(binding.etAppName.text.toString())
                val link = createPartFromString(binding.etRepoLink.text.toString())
                val type = createPartFromString(selectedRadio.toString())
                viewModel.postPortfolio(name,link,type,img, id)
                setResult(Activity.RESULT_OK)
                finish()
            }

            binding.btnSave.setOnClickListener {
                setRadioSelected()
                val idPortfolio = dataSet.idPortfolio
                val idWorker = createPartFromString(sharedPref.getString(Constants.KEY_ID_USER)!!)
                val name = createPartFromString(binding.etAppName.text.toString())
                val link = createPartFromString(binding.etRepoLink.text.toString())
                val type = createPartFromString(selectedRadio.toString())
                viewModel.patchPortfolio(idPortfolio, name,link,type,img, idWorker)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }
    fun setRadioSelected(){
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val checkRadio = checkedId
            if (checkRadio == binding.radioMobile.id) {
                selectedRadio = "mobile"
                Toast.makeText(this, selectedRadio, Toast.LENGTH_SHORT).show()
            } else if (checkRadio == binding.radioWeb.id) {
                selectedRadio = "web"
                Toast.makeText(this, selectedRadio, Toast.LENGTH_SHORT).show()
            } else {
                selectedRadio = null.toString()
                Toast.makeText(this, selectedRadio, Toast.LENGTH_SHORT).show()
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