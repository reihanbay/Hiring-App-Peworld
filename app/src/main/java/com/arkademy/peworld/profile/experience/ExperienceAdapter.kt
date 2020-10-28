package com.arkademy.peworld.profile.experience

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.ItemExperienceBinding
import com.arkademy.peworld.utils.model.ExperienceModel

class ExperienceAdapter(val items: ArrayList<ExperienceModel>) :
        RecyclerView.Adapter<ExperienceAdapter.experienceViewHolder>() {

    fun addList(list: List<ExperienceModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class experienceViewHolder(val binding: ItemExperienceBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): experienceViewHolder {
        return experienceViewHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_experience,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: experienceViewHolder, position: Int) {
        val item = items[position]

        val words = item.workPosition.split(" ")
        var work = ""
        words.forEach {
            work += it.capitalize() + " "
        }
        work.trimEnd()
        holder.binding.tvJob.text = work
        holder.binding.tvCompany.text = item.companyName
        holder.binding.tvDateJob.text = "${item.start} - ${item.end}"
        holder.binding.tvSummaryJob.text = item.description
    }

    override fun getItemCount(): Int = items.size
}