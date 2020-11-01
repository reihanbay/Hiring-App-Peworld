package com.arkademy.peworld.utils.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.ContainerRvWorkerBinding
import com.arkademy.peworld.utils.model.WorkerModel
import com.bumptech.glide.Glide

class RecyclerWorkerAdapter(val items: ArrayList<WorkerModel>, val listener: OnClickViewListener) :
    RecyclerView.Adapter<RecyclerWorkerAdapter.WorkerViewHolder>() {

    class WorkerViewHolder(val binding: ContainerRvWorkerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerViewHolder {
        return WorkerViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.container_rv_worker, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: WorkerViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView).load("http://34.229.16.81:8008/uploads/${item.image}")
            .placeholder(R.drawable.ic_user).into(holder.binding.ivProfile)
        holder.binding.ivProfile.clipToOutline = true
        holder.binding.tvNameProfile.text = item.name
        holder.binding.tvRoleJob.text = item.title
        if (item.skill != "Not Any Skill") {
        var skill = item.skill.split(",")
            holder.binding.tvSkill1.text = skill[0]
            if (skill.size > 1) {
                holder.binding.tvSkill2.text = skill[1]
                holder.binding.tvSkillPlus.text = "${skill.size - 2}+"
            } else {
                holder.binding.tvSkillPlus.visibility = View.GONE
                holder.binding.tvSkill2.visibility = View.GONE
            }
        } else {
            holder.binding.tvSkillPlus.text = item.skill
            holder.binding.tvSkill1.visibility = View.GONE
            holder.binding.tvSkill2.visibility = View.GONE
        }
        holder.binding.container.setOnClickListener {
            listener.OnClick(item.idWorker)
        }

    }

    override fun getItemCount(): Int = items.size

    fun addList(list: List<WorkerModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    interface  OnClickViewListener {
        fun OnClick(id: Int?)
    }
}