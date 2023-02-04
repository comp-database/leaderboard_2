package com.example.leaderboardone.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.leaderboardone.Model.StudentDetails
import com.example.leaderboardone.R

class RvAdapter(private val dataset : ArrayList<StudentDetails>)
    : RecyclerView.Adapter<RvAdapter.ItemViewHolder>() {
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        val emailTV : TextView = view.findViewById(R.id.email)
        val id : TextView = view.findViewById(R.id.id)
        val nameTV : TextView = view.findViewById(R.id.name)
        val pointsTV : TextView = view.findViewById(R.id.points)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item,parent,false)//inflate actual item view
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item : StudentDetails = dataset[position]
        holder.nameTV.text = item.fullName
        holder.emailTV.text = item.collegeEmail
        holder.pointsTV.text = item.points.toString()
        holder.id.text = item.idNumber
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}