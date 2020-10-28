package com.arkademy.peworld.profile.portfolio

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.FragmentPortfolioBinding
import com.arkademy.peworld.utils.api.ApiClient
import com.arkademy.peworld.utils.api.service.PortfolioService
import com.arkademy.peworld.utils.sharedpreference.Constants
import com.arkademy.peworld.utils.sharedpreference.PreferenceHelper

class PortfolioFragment : Fragment() {
    private lateinit var binding : FragmentPortfolioBinding
    private lateinit var viewModel: PortfolioViewModel
    private lateinit var listView : PortfolioAdapter
    private lateinit var sharedPref :  PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_portfolio,container, false)
        sharedPref = PreferenceHelper(activity as AppCompatActivity)

        viewModel = ViewModelProvider(this).get(PortfolioViewModel::class.java)
        val service = ApiClient.getApiClientToken(activity as AppCompatActivity)?.create(PortfolioService::class.java)
        if (service != null) {
            viewModel.setService(service)
        }
        val id = (activity as AppCompatActivity).intent.getStringExtra("KEY_ID_WORKER")
        val idUser = sharedPref.getString(Constants.KEY_ID_USER)
        Log.d("checkId", idUser.toString())

        sharedPref = PreferenceHelper(activity as AppCompatActivity)
        if(id != null) {
            viewModel.getPortfolio(id.toInt())
        } else {
            if (idUser != null) {
                viewModel.getPortfolio(idUser.toString().toInt())
            }
        }
        subscribeLiveData()
        setListView()
        return binding.root
    }

    private fun subscribeLiveData(){
        viewModel.portfolioLiveData.observe(this, Observer {
            (binding.lvPortfolio.adapter as PortfolioAdapter).addList(it)
        })
        viewModel.isEmptyLiveData.observe(this, Observer {
            if(it){
                binding.llEmpty.visibility = View.VISIBLE
                binding.lvPortfolio.visibility = View.GONE
            }else {
                binding.llEmpty.visibility = View.GONE
                binding.lvPortfolio.visibility = View.VISIBLE
            }
        })
    }

    private fun setListView(){
        listView = PortfolioAdapter(arrayListOf())
        binding.lvPortfolio.adapter = listView


    }

}