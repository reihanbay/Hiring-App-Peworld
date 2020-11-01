package com.arkademy.peworld.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arkademy.peworld.utils.api.response.GetWorkerByIdResponse
import com.arkademy.peworld.utils.api.response.ListSkillResponse
import com.arkademy.peworld.utils.api.response.PostProfileResponse
import com.arkademy.peworld.utils.api.response.SkillResponse
import com.arkademy.peworld.utils.api.service.SkillService
import com.arkademy.peworld.utils.api.service.WorkerService
import com.arkademy.peworld.utils.model.SkillModel
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlin.coroutines.CoroutineContext

class ProfileViewModel: ViewModel(), CoroutineScope {
    private lateinit var service: WorkerService
    private lateinit var serviceSkill: SkillService
    val workerLiveData = MutableLiveData<GetWorkerByIdResponse>()
    override val coroutineContext: CoroutineContext
    get() = Job() + Dispatchers.Main

    val toastUpdateLiveData = MutableLiveData<String>()
    val isProgressLiveData = MutableLiveData<Boolean>()
    val listSkillLiveData = MutableLiveData<List<SkillModel>>()
    val isSuccessSkillLiveData = MutableLiveData<Boolean>()

    fun setService(service: WorkerService) {
        this.service = service
    }

    fun setServiceSkill(service: SkillService) {
        this.serviceSkill = service
    }

    fun getWorkerId(id: Int){
        launch {
            isProgressLiveData.value = true
            val response = withContext(Dispatchers.IO){
                try {
                    service.getWorkerById(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is GetWorkerByIdResponse)
                if(response.success == true) {
                    workerLiveData.value = response
            }
            isProgressLiveData.value = false
        }
    }

    fun patchWorkerId(id: Int?, nameWorker: RequestBody?, jobTitle: RequestBody?,statusJob: RequestBody?,city: RequestBody?,workPlace: RequestBody?,description: RequestBody?, image: MultipartBody.Part?){
        launch {
            isProgressLiveData.value = true
            val response = withContext(Dispatchers.IO){
                try {
                    service.patchWorker(id, nameWorker,jobTitle,statusJob,city,workPlace,description, image)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if(response is PostProfileResponse) {
                toastUpdateLiveData.value = response.message
            }
            isProgressLiveData.value = false
        }
    }

    fun getSkill(id: Int?) {
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    serviceSkill.getSkill(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is ListSkillResponse) {
                if (response.messages == "There is no Skill on list") {
                    isSuccessSkillLiveData.value = false
                } else {
                    isSuccessSkillLiveData.value = true
                    val list = response.data.map {
                        SkillModel(it.idSkill.toString().toInt(), it.idWorker.toString().toInt(), it.skill.orEmpty())
                    }
                    listSkillLiveData.value = list
                }

            }
        }
    }

    fun deleteSkill(id: Int?) {
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    serviceSkill.deleteSkill(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is SkillResponse) {

            }
        }
    }

    fun postSkill(idWorker: Int?, skill: String?) {
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    serviceSkill.postSkill(idWorker, skill)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is SkillResponse) {

            }
        }
    }
}