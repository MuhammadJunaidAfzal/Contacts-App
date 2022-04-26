package com.example.mycontacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val contact_list: List<Contact>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemholder, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val contactName: TextView = itemView.findViewById(R.id.contact_name)
        val contactNumber: TextView = itemView.findViewById(R.id.contact_number)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val temp: Contact = contact_list[position]
        holder.contactName.text = temp.name
        holder.contactNumber.text = temp.number
    }

    override fun getItemCount(): Int {
        return contact_list.size
    }


}