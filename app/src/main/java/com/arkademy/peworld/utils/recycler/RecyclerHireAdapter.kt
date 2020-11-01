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
        val time = item.createdAt
        var date = ""
        for (a in 0..9) {
            date += time[a]
        }
        val getDate = date.split("-")
        val day = getDate[2]
        val month = getDate[1]
        val year = getDate[0]

        when(month){
            "01" -> "January"
            "02" -> "February"
            "03" -> "March"
            "04" -> "April"
            "05" -> "May"
            "06" -> "June"
            "07" -> "July"
            "08" -> "Agustus"
            "09" -> "September"
            "10" -> "Oktober"
            "11" -> "November"
            "12" -> "Desember"
        }

        holder.binding.tvCreateJob.text = "$day-$month-$year"
        holder.binding.ivCompany.clipToOutline = true
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