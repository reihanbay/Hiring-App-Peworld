package com.arkademy.peworld.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.FragmentHomeBinding
import com.arkademy.peworld.profile.ProfileWorkerActivity
import com.arkademy.peworld.utils.api.ApiClient
import com.arkademy.peworld.utils.api.service.WorkerService
import com.arkademy.peworld.utils.model.WorkerModel
import com.arkademy.peworld.utils.recycler.RecyclerWorkerAdapter
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var recyclerViewAndroid : RecyclerWorkerAdapter
    private lateinit var recyclerViewWeb : RecyclerWorkerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val service = ApiClient.getApiClientToken(activity as AppCompatActivity)?.create(WorkerService::class.java)
        viewModel = ViewModelProvider(activity as AppCompatActivity).get(HomeViewModel::class.java)
        if (service != null) {
            viewModel.setService(service)
        }
        viewModel.getAndroid()
        viewModel.getWeb()
        subscribeLiveData()

        setUpRecyclerView()
        binding.ivNotify.setOnClickListener { startActivity(Intent(activity, NotifyActivity:: class.java)) }
        return binding.root
    }

    private fun subscribeLiveData(){
        viewModel.workerWebLiveData.observe(this, Observer {
            (binding.rvWeb.adapter as RecyclerWorkerAdapter).addList(it)
        })
        viewModel.workerAndroidLiveData.observe(this, Observer {
            (binding.rvAndroid.adapter as RecyclerWorkerAdapter).addList(it)
        })
        viewModel.isEmptyAndroid.observe(this, Observer {
            if(it){
                binding.tvEmptyWorkerAndroid.visibility = View.GONE
                binding.rvAndroid.visibility = View.VISIBLE
            } else {
                binding.tvEmptyWorkerAndroid.visibility = View.VISIBLE
                binding.rvAndroid.visibility = View.GONE
            }
        })
        viewModel.isEmptyWeb.observe(this, Observer {
            if(it){
                binding.tvEmptyWorkerWeb.visibility = View.GONE
                binding.rvWeb.visibility = View.VISIBLE
            } else {
                binding.tvEmptyWorkerWeb.visibility = View.VISIBLE
                binding.rvWeb.visibility = View.GONE
            }
        })
        viewModel.isProgressAndroid.observe(this, Observer {
            if(it){
                binding.progressBarWorkerAndroid.visibility = View.VISIBLE
            } else {
                binding.progressBarWorkerAndroid.visibility = View.GONE

            }
        })
        viewModel.isProgressWeb.observe(this, Observer {
            if(it){
                binding.progressBarWorkerWeb.visibility = View.VISIBLE
            } else {
                binding.progressBarWorkerWeb.visibility = View.GONE

            }
        })
    }

    private fun setUpRecyclerView(){
        recyclerViewAndroid = RecyclerWorkerAdapter(arrayListOf(), object: RecyclerWorkerAdapter.OnClickViewListener{
            override fun OnClick(id: Int?) {
                if (id != null) {
                    val intent = Intent(activity, ProfileWorkerActivity::class.java)
                    intent.putExtra("KEY_ID_WORKER", id.toString())
                    startActivity(intent)
                }
            }
        })
        recyclerViewWeb = RecyclerWorkerAdapter(arrayListOf(), object: RecyclerWorkerAdapter.OnClickViewListener{
            override fun OnClick(id: Int?) {
                if (id != null) {
                    val intent = Intent(activity, ProfileWorkerActivity::class.java)
                    intent.putExtra("KEY_ID_WORKER", id.toString())
                    startActivity(intent)
                }
            }
        })
        binding.rvAndroid.adapter = recyclerViewAndroid
        binding.rvWeb.adapter = recyclerViewWeb
        binding.rvAndroid.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        binding.rvWeb.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
    }

}