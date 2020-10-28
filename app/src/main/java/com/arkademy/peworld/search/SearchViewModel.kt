package com.arkademy.peworld.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arkademy.peworld.utils.api.response.GetWorkerResponse
import com.arkademy.peworld.utils.api.service.WorkerService
import com.arkademy.peworld.utils.model.WorkerModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SearchViewModel : ViewModel(), CoroutineScope {
    private lateinit var service: WorkerService
    val workerLiveData = MutableLiveData<List<WorkerModel>>()
    val isProgressLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: WorkerService) {
        this.service = service
    }

    fun getSearch(){
        launch {
            isProgressLiveData.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    service.getAllWorker()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is GetWorkerResponse) {
                val list = response.data.map {
                    WorkerModel(it.idWorker, it.image.orEmpty(),it.name.orEmpty(), it.title.orEmpty(), it.skill.orEmpty())
                }
                workerLiveData.value = list
            }
         isProgressLiveData.value = false
        }
    }
}