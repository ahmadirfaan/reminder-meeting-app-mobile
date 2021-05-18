package com.irfaan.remindermeetings.presentations.listpeople

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.irfaan.remindermeetings.data.models.Employee
import com.irfaan.remindermeetings.databinding.LayoutRvUsersBinding

class ListPeopleViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val binding = LayoutRvUsersBinding.bind(view)

    fun bind(employee: Employee) {
        binding.apply {
            nameUsers.text = "Name : ${employee.name}"
            emailUsers.text = "Email : ${employee.email}"
            imageChecked.visibility = if (employee.isChecked) View.VISIBLE else View.GONE
            view.setOnClickListener {
                employee.isChecked = !employee.isChecked
                imageChecked.visibility = if (employee.isChecked) View.VISIBLE else View.GONE
            }
        }
    }
}