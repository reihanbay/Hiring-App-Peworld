package com.arkademy.peworld.profile.portfolio

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arkademy.peworld.utils.api.response.PortfolioResponse
import com.arkademy.peworld.utils.api.service.PortfolioService
import com.arkademy.peworld.utils.model.PortfolioModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PortfolioViewModel: ViewModel(), CoroutineScope{
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: PortfolioService
    val portfolioLiveData = MutableLiveData<List<PortfolioModel>>()
    val isEmptyLiveData = MutableLiveData<Boolean>()


    fun setService(service: PortfolioService){
        this.service = service
    }

    fun getPortfolio(id: Int){
        launch {
            val response = withContext(Dispatchers.IO){
                try {
                    service.getAllPortfolioById(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is PortfolioResponse) {
                if(response.data == null || response.message == "There is no Portfolio on list") {
                    isEmptyLiveData.value = true
                } else {
                    isEmptyLiveData.value = false
                    val list = response.data.map {
                        PortfolioModel(it.idPortfolio, it.imagePortfolio, it.linkRepo, it.typePortfolio,it.namePortfolio, it.idWorker)
                    }
                    portfolioLiveData.value = list
                }
            }
        }
    }

}