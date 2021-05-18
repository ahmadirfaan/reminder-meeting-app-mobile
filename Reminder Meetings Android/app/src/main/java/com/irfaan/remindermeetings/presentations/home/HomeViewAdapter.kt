package com.irfaan.remindermeetings.presentations.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.irfaan.remindermeetings.R
import com.irfaan.remindermeetings.data.models.ScheduleMeetings

class HomeViewAdapter(private val homeClickListener: HomeClickListener) : RecyclerView.Adapter<HomeViewHolder>() {

    private var data : List<ScheduleMeetings> = ArrayList<ScheduleMeetings>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_rv_meetings, parent, false)
        return HomeViewHolder(view, homeClickListener)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val product = data[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = data.size

    fun setScheduleMeetingList(newMeetingList: List<ScheduleMeetings>) {
        this.data = newMeetingList
        notifyDataSetChanged()
    }

}