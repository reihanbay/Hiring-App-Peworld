package com.arkademy.peworld.hire

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
import com.arkademy.peworld.databinding.ActivityLoginBinding.bind
import com.arkademy.peworld.databinding.ActivityLoginBinding.inflate
import com.arkademy.peworld.databinding.FragmentHireBinding
import com.arkademy.peworld.databinding.FragmentHomeBinding
import com.arkademy.peworld.profile.ProfileWorkerActivity
import com.arkademy.peworld.utils.api.ApiClient
import com.arkademy.peworld.utils.api.service.HireService
import com.arkademy.peworld.utils.recycler.RecyclerHireAdapter
import com.arkademy.peworld.utils.sharedpreference.Constants
import com.arkademy.peworld.utils.sharedpreference.PreferenceHelper

class HireFragment : Fragment() {

    private lateinit var binding: FragmentHireBinding
    private lateinit var viewModel: HireViewModel
    private lateinit var recyclerView : RecyclerHireAdapter
    private lateinit var sharedPref : PreferenceHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hire, container, false)
        sharedPref = PreferenceHelper(activity as AppCompatActivity)
        viewModel = ViewModelProvider(activity as AppCompatActivity).get(HireViewModel::class.java)
        val service = ApiClient.getApiClientToken(activity as AppCompatActivity)?.create(HireService::class.java)
        if (service != null) {
            viewModel.setService(service)
        }
        val id = sharedPref.getString(Constants.KEY_ID_USER)
        viewModel.getHireList(id.toString().toInt())

        subscribeLiveData()
        setRecyclerView()
        return binding.root
    }

    private fun subscribeLiveData() {
        viewModel.isEmptyLiveData.observe(this, Observer {
            if (it) {
                binding.icEmptyHire.visibility = View.VISIBLE
                binding.rvHire.visibility = View.GONE
            } else {
                binding.icEmptyHire.visibility = View.GONE
                binding.rvHire.visibility = View.VISIBLE
            }
        })

        viewModel.hireLiveData.observe(this, Observer {
            (binding.rvHire.adapter as RecyclerHireAdapter).addList(it)
        })
    }

    private fun setRecyclerView(){
        recyclerView = RecyclerHireAdapter(arrayListOf(), object : RecyclerHireAdapter.OnClickViewListener {
            override fun OnClick(id: Int?) {
                Toast.makeText(activity, id.toString(), Toast.LENGTH_SHORT).show()
                val intent = Intent(activity, DetailHireActivity::class.java)

                intent.putExtra("KEY_ID_HIRE", id.toString())
                startActivity(intent)
            }
        })
        binding.rvHire.adapter = recyclerView
        binding.rvHire.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

}