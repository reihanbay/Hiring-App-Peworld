package com.arkademy.peworld.profile.portfolio

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.FragmentPortfolioBinding
import com.arkademy.peworld.databinding.ItemDialogPortfolioBinding
import com.arkademy.peworld.profile.experience.ExperienceAdapter
import com.arkademy.peworld.profile.form.FormPortfolioActivity
import com.arkademy.peworld.profile.form.FormPortfolioViewModel
import com.arkademy.peworld.utils.api.ApiClient
import com.arkademy.peworld.utils.api.service.PortfolioService
import com.arkademy.peworld.utils.model.PortfolioModel
import com.arkademy.peworld.utils.sharedpreference.Constants
import com.arkademy.peworld.utils.sharedpreference.PreferenceHelper
import com.bumptech.glide.Glide

class PortfolioFragment : Fragment() {
    private lateinit var binding : FragmentPortfolioBinding
    private lateinit var viewModel: PortfolioViewModel
    private lateinit var recyclerView : PortfolioAdapter
    private lateinit var sharedPref :  PreferenceHelper

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
        sharedPref = PreferenceHelper(activity as AppCompatActivity)
        val idUser = sharedPref.getString(Constants.KEY_ID_USER)

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
        val id = (activity as AppCompatActivity).intent.getStringExtra("KEY_ID_WORKER")
        sharedPref = PreferenceHelper(activity as AppCompatActivity)
        val idUser = sharedPref.getString(Constants.KEY_ID_USER)
        var code : String? = null
        if (id != null) {
            code = "WORKER"
        } else {
            if(idUser != null) {
                code = "USER"
            } else {
                code = null
            }
        }
        recyclerView = PortfolioAdapter(code, arrayListOf(), object : PortfolioAdapter.OnClickViewListener {
            override fun OnClickPortofolio(data: PortfolioModel?) {
                if (data != null) {
                    dialog(data, code)
                }
            }
            override fun OnClickAdd(idWorker: Int?) {
                val intent = Intent(activity, FormPortfolioActivity::class.java)
                intent.putExtra("CODE", "ADD")
                startActivityForResult(intent, FormPortfolioActivity.CODE_CONFIRM)
            }

        })
        binding.lvPortfolio.adapter = recyclerView
        binding.lvPortfolio.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel = ViewModelProvider(this).get(PortfolioViewModel::class.java)
        val service = ApiClient.getApiClientToken(activity as AppCompatActivity)?.create(PortfolioService::class.java)
        if (service != null) {
            viewModel.setService(service)
        }
        val id = (activity as AppCompatActivity).intent.getStringExtra("KEY_ID_WORKER")
        sharedPref = PreferenceHelper(activity as AppCompatActivity)
        val idUser = sharedPref.getString(Constants.KEY_ID_USER)

        if (resultCode == Activity.RESULT_OK && requestCode == FormPortfolioActivity.CODE_CONFIRM) {
            if(id != null) {
                viewModel.getPortfolio(id.toInt())
            } else {
                if (idUser != null) {
                    viewModel.getPortfolio(idUser.toString().toInt())
                }
            }
            subscribeLiveData()
            setListView()
        }
    }

    private fun dialog(data: PortfolioModel?, code: String?){
        val view = DataBindingUtil.inflate<ItemDialogPortfolioBinding>(layoutInflater, R.layout.item_dialog_portfolio, null, false)
        val dialog = AlertDialog.Builder(activity as AppCompatActivity)
                .setView(view.root)
                .setCancelable(true)
                .create()
                dialog.show()
        view.tvNamePortfolio.text = data?.namePortfolio
        view.tvValueType.text = data?.typePortfolio
        view.tvValueLink.text = data?.linkRepo
        Glide.with(this).load("http://34.229.16.81:8008/uploads/${data?.image}")
                .placeholder(R.drawable.ic_user).into(view.ivPortfolio)
        if (code == "WORKER") {
            view.btnSetting.visibility = View.GONE
        } else if ( code == "USER"){
            view.btnSetting.visibility = View.VISIBLE
        }
        val data = PortfolioModel(data?.idPortfolio.toString().toInt(), data?.image.toString(), data?.linkRepo.toString(), data?.typePortfolio.toString(), data?.namePortfolio.toString(), data?.idWorker.toString().toInt())
        view.btnSetting.setOnClickListener {
            val intent = Intent(activity, FormPortfolioActivity::class.java)
            intent.putExtra("CODE", "EDIT")
            intent.putExtra("DATA_PARCELIZE", data)
            startActivityForResult(intent, FormPortfolioActivity.CODE_CONFIRM)
        }
    }

}