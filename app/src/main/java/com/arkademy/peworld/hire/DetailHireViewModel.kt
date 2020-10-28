package com.arkademy.peworld.hire

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arkademy.peworld.utils.api.response.GetHireByIdResponse
import com.arkademy.peworld.utils.api.response.PatchHireResponse
import com.arkademy.peworld.utils.api.service.HireService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DetailHireViewModel: ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service : HireService
    val getHireLiveData = MutableLiveData<GetHireByIdResponse>()
    val patchHireLiveData = MutableLiveData<PatchHireResponse>()
    val isSuccessLiveData = MutableLiveData<Boolean>()
    fun setService(service: HireService){
        this.service = service
    }

    fun getHireDetail(id: Int){
        launch {
            val response = withContext(Dispatchers.IO){
                try {
                    service.getHireById(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if(response is GetHireByIdResponse) {
                getHireLiveData.value = response
            }
        }
    }

    fun confirmHire(id: Int,statusConfirm: String?){
        launch {
            val response = withContext(Dispatchers.IO){
                try {
                    service.patchConfirm(id,statusConfirm)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is PatchHireResponse) {
                if (response.data?.statusConfirm != null) {
                    patchHireLiveData.value = response
                }
            }
        }
    }

}