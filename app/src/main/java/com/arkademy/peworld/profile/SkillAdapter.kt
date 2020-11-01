package com.arkademy.peworld.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.ItemSkillBinding
import com.arkademy.peworld.utils.model.SkillModel

class SkillAdapter(val code: String?, val items: ArrayList<SkillModel>, val listener: OnClickViewListener): RecyclerView.Adapter<SkillAdapter.SkillViewHolder>() {


    fun addList(list: List<SkillModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class SkillViewHolder(val binding: ItemSkillBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnClickViewListener {
        fun OnClick(id: Int?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        return SkillViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_skill, parent, false))

    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        val item = items[position]

        if (items.size == 0) {
            holder.binding.tvSkill.text = "Not Any Skill"
        }
        holder.binding.tvSkill.text = item.skill
        if (code == "VIEW") {
            holder.binding.btnRemove.visibility = View.GONE
        } else if (code == "EDITABLE"){
            holder.binding.btnRemove.visibility = View.VISIBLE
            holder.binding.btnRemove.setOnClickListener {
                listener.OnClick(item.idSkill)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}