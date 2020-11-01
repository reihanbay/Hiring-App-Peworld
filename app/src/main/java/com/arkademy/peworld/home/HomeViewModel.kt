package com.arkademy.peworld.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arkademy.peworld.utils.api.response.GetWorkerResponse
import com.arkademy.peworld.utils.api.service.WorkerService
import com.arkademy.peworld.utils.model.WorkerModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class HomeViewModel: ViewModel(),CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: WorkerService
    val isEmptyAndroid = MutableLiveData<Boolean>()
    val isEmptyWeb = MutableLiveData<Boolean>()
    val isProgressAndroid = MutableLiveData<Boolean>()
    val isProgressWeb = MutableLiveData<Boolean>()
    val workerAndroidLiveData = MutableLiveData<List<WorkerModel>>()
    val workerWebLiveData = MutableLiveData<List<WorkerModel>>()


    fun setService(service: WorkerService) {
        this.service = service
    }

    fun getAndroid(){
        launch {
            isProgressAndroid.value = true
            val response = withContext(Job()+Dispatchers.IO){
                try {
                    service.getAllWorker("Android")
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is GetWorkerResponse){
                isEmptyAndroid.value = response.success
                val list = response.data.map {
                    WorkerModel(it.idWorker, it.image.orEmpty(),it.name.orEmpty(), it.title.orEmpty(), it.statusJob.orEmpty(), it.city.orEmpty(), it.skill.orEmpty())
                }
                workerAndroidLiveData.value = list
            }
            isProgressAndroid.value = false
        }
    }
    fun getWeb(){
        launch {
            isProgressWeb.value = true
            val response = withContext(Job()+Dispatchers.IO){
                try {
                    service.getAllWorker("Web")
                } catch (e: Throwable){
                    e.printStackTrace()
                }
            }
            if (response is GetWorkerResponse) {
//                if (response.message == "jwt malformed")
                isEmptyWeb.value = response.success
                val list = response.data.map {
                    WorkerModel(it.idWorker, it.image.orEmpty(),it.name.orEmpty(), it.title.orEmpty(), it.statusJob.orEmpty(), it.city.orEmpty(), it.skill.orEmpty())
                }
                workerWebLiveData.value = list
            }
            isProgressWeb.value = false
        }
    }
}