package com.arkademy.peworld.search

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
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
import com.arkademy.peworld.utils.model.WorkerModel
import com.arkademy.peworld.utils.recycler.RecyclerWorkerAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var recyclerAdapter: RecyclerWorkerAdapter
    lateinit var sv: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        val toolbar = binding.toolbarMain
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val supportActionBar = (activity as AppCompatActivity).supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setTitle("Pencarian")


        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        val service = ApiClient.getApiClientToken(activity as AppCompatActivity)?.create(WorkerService::class.java)
        if (service != null) {
            viewModel.setService(service)
        }
        viewModel.getSearch("", "nameWorker", "asc")
        binding.btnFilter.setOnClickListener {
            dialogFilter()
        }
        subsribeLiveData()
        setRecyclerView()

        return binding.root
    }

    private fun subsribeLiveData() {
        viewModel.workerLiveData.observe(this, Observer {
            (binding.rvWorker.adapter as RecyclerWorkerAdapter).addList(it)

            sv = binding.svSearch

            sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    var result: ArrayList<WorkerModel> = ArrayList()
                    for (find in it) {
                        if (find.name.contains(query) || find.title.contains(query) || find.city.contains(query) || find.status.contains(query) || find.skill.contains(query)) {
                            result.add(find)
                        }
                    }
                    (binding.rvWorker.adapter as RecyclerWorkerAdapter).addList(result)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    var result: ArrayList<WorkerModel> = ArrayList()
                    for (find in it) {
                        if (find.name.contains(newText) || find.title.contains(newText) || find.city.contains(newText) || find.status.contains(newText) || find.skill.contains(newText)) {
                            result.add(find)
                        }
                    }
                    (binding.rvWorker.adapter as RecyclerWorkerAdapter).addList(result)
                    return false
                }
            })
        })
        viewModel.isProgressLiveData.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun setRecyclerView() {
        recyclerAdapter = RecyclerWorkerAdapter(arrayListOf(), object : RecyclerWorkerAdapter.OnClickViewListener {
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


    private fun dialogFilter(){
        val singleItems = arrayOf("Filter Berdasar Nama", "Filter Berdasar Skill", "Filter Berdasar Lokasi", "Filter Berdasar Freelance", "Filter Berdasar Fulltime")
        MaterialAlertDialogBuilder(activity as AppCompatActivity)
                // Single-choice items (initialized with checked item)
                .setCancelable(true)
                .setItems(singleItems) { dialog, which ->
                    when (which) {
                        0 -> {
                            viewModel.getSearch("","nameWorker", "asc")
                            dialog.dismiss()
                        }
                        1 -> {
                            viewModel.getSearch("","skill", "asc")
                            dialog.dismiss()
                        }
                        2 -> {
                            viewModel.getSearch("","city", "asc")
                            dialog.dismiss()
                        }
                        3 -> {
                            viewModel.getSearch("freelance","nameWorker", "asc")
                            dialog.dismiss()
                        }
                        4 -> {
                            viewModel.getSearch("full","nameWorker", "asc")
                            dialog.dismiss()
                        }
                    }
                }
                .create()
                .show()
    }
}