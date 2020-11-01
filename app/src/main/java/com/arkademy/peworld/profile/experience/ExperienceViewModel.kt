package com.arkademy.peworld.profile.experience

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arkademy.peworld.utils.api.response.ListExperienceResponse
import com.arkademy.peworld.utils.api.service.ExperienceService
import com.arkademy.peworld.utils.model.ExperienceModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ExperienceViewModel: ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: ExperienceService
    val experienceLiveData = MutableLiveData<List<ExperienceModel>>()
    val isEmptyLiveData = MutableLiveData<Boolean>()

    fun setServiceExperience(service: ExperienceService){
        this.service = service
    }

    fun getExperience(id: Int){
        launch {
            val response = withContext(Dispatchers.IO){
                try {
                    service.getAllExperienceById(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is ListExperienceResponse) {
                if(response.data == null || response.message == "There is no Experience on list") {
                    isEmptyLiveData.value = true
                } else {
                    isEmptyLiveData.value = false
                    val list = response.data.map {
                        ExperienceModel(it.idExperience,it.companyName,it.description,it.workPosition,it.start,it.end,it.idWorker)
                    }

                    experienceLiveData.value = list
                }
            }
        }
    }
}