package com.arkademy.peworld.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arkademy.peworld.utils.api.response.RegisterResponse
import com.arkademy.peworld.utils.api.service.AccountService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RegisterViewModel: ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service : AccountService
    val isProgressLiveData = MutableLiveData<Boolean>()
    val toastLiveData = MutableLiveData<String>()
    val isSuccessLiveData = MutableLiveData<Boolean>()


    fun setService(service:AccountService){
        this.service = service
    }

    fun postRegister(name: String, email: String, password: String, noHp: String, status: Int){
        launch {
            isProgressLiveData.value = true
            val response = withContext(Dispatchers.IO){
                try {
                    service.register(name, email, password, noHp, status, "worker")
                } catch (e : Throwable){
                    e.printStackTrace()
                    toastLiveData.value = e.message
                }
            }
            if (response is RegisterResponse){
                toastLiveData.value = response.message
                    if (response.success == true) {
                        isSuccessLiveData.value = true
                    } else {
                        toastLiveData.value = response.message
                    }
            }
            isProgressLiveData.value = false
        }
    }
}