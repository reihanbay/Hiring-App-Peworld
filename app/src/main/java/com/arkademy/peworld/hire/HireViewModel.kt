package com.arkademy.peworld.hire

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arkademy.peworld.utils.api.response.GetHireResponse
import com.arkademy.peworld.utils.api.service.HireService
import com.arkademy.peworld.utils.model.HireModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class HireViewModel : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: HireService
    val isEmptyLiveData = MutableLiveData<Boolean>()
    val hireLiveData = MutableLiveData<List<HireModel>>()

    fun setService(service: HireService) {
        this.service = service
    }

    fun getHireList(id: Int) {
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.getHire(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is GetHireResponse) {
                if (response.data == null) {
                    isEmptyLiveData.value = true
                } else {
                    isEmptyLiveData.value = false
                    val list = response.data.map {
                        HireModel(it.idHire.toString().toInt(), it.image.toString(), it.projectJob.toString(), it.statusConfirm.toString(), it.createdAt.toString(), it.recruiter.toString())
                    }
                    hireLiveData.value = list
                }
            }
        }
    }
}