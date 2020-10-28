package com.arkademy.peworld.profile.form

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arkademy.peworld.utils.api.response.PostProfileResponse
import com.arkademy.peworld.utils.api.service.WorkerService
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlin.coroutines.CoroutineContext

class FormProfileViewModel: ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service : WorkerService
    val isToastLiveData = MutableLiveData<String>()
    val isSuccessLiveData = MutableLiveData<Boolean>()

    fun setService(service: WorkerService){
        this.service = service
    }

    fun postDataProfile(nameWorker: RequestBody?, jobTitle: RequestBody?, statusJob: RequestBody?, city: RequestBody?, workPlace: RequestBody?, description: RequestBody?, idAccount: RequestBody?, image: MultipartBody.Part) {
        launch {
            val response = withContext(Dispatchers.IO){
                try {
                    service.postWorker(nameWorker,jobTitle,statusJob,city,workPlace,description,idAccount,image)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is PostProfileResponse){
                isToastLiveData.value = response.message
                isSuccessLiveData.value = response.success
            }
        }
    }
}