package com.arkademy.peworld.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arkademy.peworld.utils.api.response.GetWorkerByIdResponse
import com.arkademy.peworld.utils.api.service.WorkerService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ProfileWorkerViewModel : ViewModel(), CoroutineScope {
    private lateinit var service: WorkerService
    val workerLiveData = MutableLiveData<GetWorkerByIdResponse>()
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: WorkerService) {
        this.service = service
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
}