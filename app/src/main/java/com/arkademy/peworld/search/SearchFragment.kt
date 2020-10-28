package com.arkademy.peworld.search

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.FragmentSearchBinding
import com.arkademy.peworld.profile.ProfileWorkerActivity
import com.arkademy.peworld.utils.api.ApiClient
import com.arkademy.peworld.utils.api.service.WorkerService
import com.arkademy.peworld.utils.recycler.RecyclerWorkerAdapter

class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var recyclerAdapter: RecyclerWorkerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        val service = ApiClient.getApiClientToken(activity as AppCompatActivity)?.create(WorkerService::class.java)
        if (service != null) {
            viewModel.setService(service)
        }
        viewModel.getSearch()
        subsribeLiveData()
        setRecyclerView()

        return binding.root
    }

    private fun subsribeLiveData() {
        viewModel.workerLiveData.observe(this, Observer {
            (binding.rvWorker.adapter as RecyclerWorkerAdapter).addList(it)
        })
        viewModel.isProgressLiveData.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun setRecyclerView(){
        recyclerAdapter = RecyclerWorkerAdapter(arrayListOf(), object: RecyclerWorkerAdapter.OnClickViewListener{
            override fun OnClick(id: Int?) {
                if (id != null) {
                    val intent = Intent(activity, ProfileWorkerActivity::class.java)
                    intent.putExtra("KEY_ID_WORKER", id.toString())
                    startActivity(intent)
                }
            }
        })

        binding.rvWorker.adapter = recyclerAdapter
        binding.rvWorker.layoutManager = GridLayoutManager(activity, 2, RecyclerView.VERTICAL, false)
    }

}