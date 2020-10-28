package com.arkademy.peworld.profile.portfolio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.arkademy.peworld.R
import com.arkademy.peworld.utils.model.PortfolioModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_portfolio.view.*

class PortfolioAdapter(val items: ArrayList<PortfolioModel>): BaseAdapter() {
    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = 0

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_portfolio, parent, false)

        val item = items[position]

        Glide.with(view.context).load("http://34.229.16.81:8008/uploads/${item.image}")
                .placeholder(R.drawable.ic_user).into(view.iv_portfolio)
        return view
    }

    fun addList(list: List<PortfolioModel>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}