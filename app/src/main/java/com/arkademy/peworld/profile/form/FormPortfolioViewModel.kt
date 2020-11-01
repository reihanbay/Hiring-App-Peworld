package com.arkademy.peworld.profile.form

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arkademy.peworld.utils.api.response.DeleteResponse
import com.arkademy.peworld.utils.api.response.PortfolioResponse
import com.arkademy.peworld.utils.api.service.PortfolioService
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlin.coroutines.CoroutineContext

class FormPortfolioViewModel: ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: PortfolioService
    val isSuccessLiveData = MutableLiveData<Boolean>()

    fun setService(service: PortfolioService){
        this.service = service
    }

    fun postPortfolio(name: RequestBody?, link: RequestBody?, type: RequestBody?, image: MultipartBody.Part?, idWorker: RequestBody?){
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.postPortfolio(name,link,type,image,idWorker)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is PortfolioResponse) {
                isSuccessLiveData.value = response.success
            }
        }
    }

    fun patchPortfolio(id: Int?, name: RequestBody?, link: RequestBody?, type: RequestBody?, image: MultipartBody.Part?, idWorker: RequestBody?){
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.patchPortfolio(id, name,link,type,image,idWorker)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is PortfolioResponse) {
                isSuccessLiveData.value = response.success
            }
        }
    }

    fun deletePortfolio(id: Int?){
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.deletePortfolio(id)
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