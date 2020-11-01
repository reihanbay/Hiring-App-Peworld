package com.arkademy.peworld.profile.portfolio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.ItemExperienceBinding
import com.arkademy.peworld.databinding.ItemPortfolioBinding
import com.arkademy.peworld.utils.model.ExperienceModel
import com.arkademy.peworld.utils.model.PortfolioModel
import com.arkademy.peworld.utils.recycler.RecyclerHireAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_portfolio.view.*

class PortfolioAdapter(val code: String?, val items: ArrayList<PortfolioModel>, val listener : OnClickViewListener) :
        RecyclerView.Adapter<PortfolioAdapter.PortfolioViewHolder>() {

    fun addList(list: List<PortfolioModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    interface OnClickViewListener {
        fun OnClickPortofolio(data: PortfolioModel?)
        fun OnClickAdd(idWorker: Int? )
    }
    class PortfolioViewHolder(val binding: ItemPortfolioBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioViewHolder {
        return PortfolioViewHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_portfolio,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: PortfolioViewHolder, position: Int) {
        val item = items[position]

        Glide.with(holder.itemView).load("http://34.229.16.81:8008/uploads/${item.image}")
                .placeholder(R.drawable.ic_user).into(holder.binding.ivPortfolio)
        holder.binding.ivPortfolio.clipToOutline = true

        if (position == items.size - 1 || items.size == 0) {
            if (code == "WORKER") {
                holder.binding.btnAddPort.visibility = View.GONE
            } else if (code == "USER") {
                holder.binding.btnAddPort.visibility = View.VISIBLE
            }
        }

        holder.binding.btnAddPort.setOnClickListener {
            listener.OnClickAdd(item.idWorker)
        }

        holder.binding.ivPortfolio.setOnClickListener {
            listener.OnClickPortofolio(PortfolioModel(
                    item.idPortfolio, item.image, item.linkRepo, item.typePortfolio, item.namePortfolio, item.idWorker
            ))
        }

    }


    override fun getItemCount(): Int = items.size
}