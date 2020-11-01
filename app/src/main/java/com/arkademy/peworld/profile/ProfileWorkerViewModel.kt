package com.arkademy.peworld.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arkademy.peworld.utils.api.response.GetWorkerByIdResponse
import com.arkademy.peworld.utils.api.response.ListSkillResponse
import com.arkademy.peworld.utils.api.service.SkillService
import com.arkademy.peworld.utils.api.service.WorkerService
import com.arkademy.peworld.utils.model.SkillModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ProfileWorkerViewModel : ViewModel(), CoroutineScope {
    private lateinit var service: WorkerService
    private lateinit var serviceSkill : SkillService
    val workerLiveData = MutableLiveData<GetWorkerByIdResponse>()
    val listSkillLiveData = MutableLiveData<List<SkillModel>>()
    val isSuccessSkillLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: WorkerService) {
        this.service = service
    }

    fun setServiceSkill(service: SkillService) {
        this.serviceSkill = service
    }

    fun getWorkerId(id: Int){
        launch {
            val response = withContext(Dispatchers.IO){
                try {
                    service.getWorkerById(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is GetWorkerByIdResponse)
                workerLiveData.value = response
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
}
