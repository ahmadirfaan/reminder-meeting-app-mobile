package com.irfaan.remindermeetings.presentations.listpeople

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.irfaan.remindermeetings.R
import com.irfaan.remindermeetings.data.models.Employee

class ListPeopleViewAdapter() : RecyclerView.Adapter<ListPeopleViewHolder>() {

    private var data: List<Employee> = ArrayList<Employee>()
    private var selected: List<Employee> = ArrayList<Employee>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPeopleViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_rv_users, parent, false)
        return ListPeopleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListPeopleViewHolder, position: Int) {
        val product = data[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = data.size

    fun setEmployeeList(newEmployeeList: List<Employee>) {
        this.data = newEmployeeList
        notifyDataSetChanged()
    }

    fun getSelectedList(): List<Employee> {
        this.selected = data.filter {
            it.isChecked
        }
        return selected
    }

}