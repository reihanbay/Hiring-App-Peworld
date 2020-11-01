package com.arkademy.peworld.profile.form

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arkademy.peworld.utils.api.response.DeleteResponse
import com.arkademy.peworld.utils.api.response.ExperienceResponse
import com.arkademy.peworld.utils.api.service.ExperienceService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class FormExperienceViewModel: ViewModel() , CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: ExperienceService
    val isSuccessLiveData = MutableLiveData<Boolean>()

    fun setService(service: ExperienceService){
        this.service = service
    }

    fun postExperience(companyName: String?,description: String?, workPosition: String? ,start: String? , end: String? ,idWorker: Int? ){
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.postExperience(companyName,description,workPosition,start, end,idWorker)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is ExperienceResponse) {
                isSuccessLiveData.value = response.success
            }
        }
    }

    fun patchExperience(id: Int? ,companyName: String?,description: String?, workPosition: String? ,start: String? , end: String? ,idWorker: Int? ){
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.patchExperience(id, companyName,description,workPosition,start, end,idWorker)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is ExperienceResponse) {
                isSuccessLiveData.value = response.success
            }
        }
    }

    fun deleteExperience(id: Int?){
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.deleteExperience(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is DeleteResponse) {
                isSuccessLiveData.value = response.success
            }
        }
    }
}