package com.arkademy.peworld.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arkademy.peworld.utils.api.response.GetWorkerResponse
import com.arkademy.peworld.utils.api.response.loginResponse
import com.arkademy.peworld.utils.api.service.AccountService
import com.arkademy.peworld.utils.api.service.WorkerService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LoginViewModel: ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var serviceCheck : WorkerService
    private lateinit var service : AccountService
    val isSuccessLiveData = MutableLiveData<Boolean>()
    val isProgressLiveData = MutableLiveData<Boolean>()
    val isToastLiveData = MutableLiveData<Boolean>()
    val responseLiveData = MutableLiveData<loginResponse>()
    val checkLiveData = MutableLiveData<GetWorkerResponse>()



    fun setService(service: AccountService){
        this.service = service
    }

    fun setServiceCheck(service: WorkerService) {
        this.serviceCheck = service
    }

    fun loginService(email: String, password: String){
        launch {
            isSuccessLiveData.value = false
            val response = withContext(Dispatchers.IO) {
                try {
                    service.login(email, password)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is loginResponse) {
                if (response.data.role == "worker") {
                    responseLiveData.value = response
                    isSuccessLiveData.value = true
                } else {
                    isToastLiveData.value = false
                }
            } else {
                isSuccessLiveData.value = false
            }
            isProgressLiveData.value = true
        }
    }

    fun checkDataApi(id: Int){
        launch {
            isProgressLiveData.value = false
            val response = withContext(Dispatchers.IO) {
                try {
                    serviceCheck.checkProfileById(id)
                } catch (e : Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is GetWorkerResponse) {
                checkLiveData.value = response
            }
            isProgressLiveData.value = true
        }
    }
}