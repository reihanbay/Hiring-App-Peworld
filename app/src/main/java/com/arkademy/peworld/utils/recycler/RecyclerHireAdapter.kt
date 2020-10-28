package com.arkademy.peworld.utils.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.ItemHireBinding
import com.arkademy.peworld.utils.model.HireModel
import com.bumptech.glide.Glide

class RecyclerHireAdapter(val items : ArrayList<HireModel>, val listener: OnClickViewListener) : RecyclerView.Adapter<RecyclerHireAdapter.HireViewHolder>() {

    class HireViewHolder(val binding: ItemHireBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HireViewHolder {
        return HireViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_hire, parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: HireViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView).load("http://34.229.16.81:8008/uploads/${item.image}")
                .placeholder(R.drawable.ic_company).into(holder.binding.ivCompany)
        holder.binding.tvCompany.text = item.recruiter
        holder.binding.tvProjectJob.text = item.projectJob
        holder.binding.tvCreateJob.text = item.createdAt
        holder.binding.ivCompany.clipToOutline
        holder.binding.container.setOnClickListener {
            listener.OnClick(item.idHire)
        }
        when(item.statusConfirm){
            "delayed" -> {
                holder.binding.tvConfirm.text = item.statusConfirm.capitalize()
                holder.binding.tvConfirm.backgroundTintList = ContextCompat.getColorStateList(holder.binding.tvConfirm.context, R.color.gray_2)
            }
            "accepted" -> {
                holder.binding.tvConfirm.text = item.statusConfirm.capitalize()
                holder.binding.tvConfirm.backgroundTintList = ContextCompat.getColorStateList(holder.binding.tvConfirm.context, R.color.green_1)
            }
            "rejected" -> {
                holder.binding.tvConfirm.text = item.statusConfirm.capitalize()
                holder.binding.tvConfirm.backgroundTintList = ContextCompat.getColorStateList(holder.binding.tvConfirm.context, R.color.red_1)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun addList(list: List<HireModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    interface  OnClickViewListener {
        fun OnClick(id: Int?)
    }
}