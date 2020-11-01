package com.arkademy.peworld.profile.experience

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arkademy.peworld.R
import com.arkademy.peworld.databinding.ItemExperienceBinding
import com.arkademy.peworld.utils.model.ExperienceModel
import com.arkademy.peworld.utils.model.PortfolioModel

class ExperienceAdapter(val code: String?, val items: ArrayList<ExperienceModel>, val listener: OnClickViewListener) :
        RecyclerView.Adapter<ExperienceAdapter.experienceViewHolder>() {

    fun addList(list: List<ExperienceModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
    interface OnClickViewListener {
        fun OnClick(data:ExperienceModel?)
        fun OnClickAdd()
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
    fun year(y : String) : Int {
        val time= y
        var date = ""
        for (a in 0..9) {
            date += time[a]
        }
        val getDate = date.split("-")
        var year = getDate[0]

        return year.toInt()
    }

    fun month(month: String) : Int {
        val time= month
        var date = ""
        for (a in 0..9) {
            date += time[a]
        }
        val getDate = date.split("-")
        var month = getDate[1]
        return month.toInt()
    }
    fun date(date: String) : String {
        val time= date
        var date = ""
        for (a in 0..9) {
            date += time[a]
        }
        val getDate = date.split("-")
        var day = getDate[2]
        var month = getDate[1]
        var year = getDate[0]

        when(month){
            "01" -> {
                month = "Jan"
            }
            "02" -> {
                month = "Feb"
            }
            "03" -> {
                month = "Mar"
            }
            "04" -> {
                month = "Apr"
            }
            "05" -> {
                month = "May"
            }
            "06" -> {
                month = "Jun"
            }
            "07" -> {
                month = "Jul"
            }
            "08" -> {
                month = "Aug"
            }
            "09" -> {
                month = "Sep"
            }
            "10" -> {
                month = "Okt"
            }
            "11" -> {
                month = "Nov"
            }
            "12" -> {
                month = "Dec"
            }
        }

        return "$month-$year"
    }
    override fun onBindViewHolder(holder: experienceViewHolder, position: Int) {
        val item = items[position]

        val words = item.workPosition.split(" ")
        var work = ""
        words.forEach {
            work += it.capitalize() + " "
        }
        work.trimEnd()

        val timeStart = date(item.start)
        val timeEnd = date(item.end)

        val longJob = if (year(item.end) - year(item.start) <= 0) {
            "${(month(item.end) - month(item.start))} Months"
        } else {
            if (month(item.end) - month(item.start) <= 0) {
            "${year(item.end) - year(item.start)} Year"
            } else {
            "${year(item.end) - year(item.start)} Years ${month(item.end) - month(item.start)} Months"
            }
        }

        holder.binding.tvLongJob.text = longJob
        holder.binding.tvJob.text = work
        holder.binding.tvCompany.text = item.companyName
        holder.binding.tvDateJob.text = "$timeStart - $timeEnd"
        holder.binding.tvSummaryJob.text = item.description

        if (position == items.size - 1 || items.size == 0) {
            if (code == "WORKER") {
                holder.binding.btnAddPort.visibility = View.GONE
            } else if (code == "USER") {
                holder.binding.btnAddPort.visibility = View.VISIBLE
            }
        }

        holder.binding.btnAddPort.setOnClickListener {
            listener.OnClickAdd()
        }

        holder.binding.containerExperience.setOnClickListener {
            listener.OnClick(ExperienceModel(
                item.idExperience, item.companyName, item.description, item.workPosition, item.start, item.end, item.idWorker
            ))
        }
    }

    override fun getItemCount(): Int = items.size
}