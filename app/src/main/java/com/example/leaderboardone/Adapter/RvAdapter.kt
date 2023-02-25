package com.example.leaderboardone.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.leaderboardone.Model.StudentDetails
import com.example.leaderboardone.R

class RvAdapter(private val dataset : ArrayList<StudentDetails>)
    : RecyclerView.Adapter<RvAdapter.ItemViewHolder>() {
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
//        val emailTV : TextView = view.findViewById(R.id.email)
        val id : TextView = view.findViewById(R.id.id)
        val nameTV : TextView = view.findViewById(R.id.name)
        val pointsTV : TextView = view.findViewById(R.id.points)
        val rank : TextView = view.findViewById(R.id.rank)
        val expand : LinearLayout = view.findViewById(R.id.expanded_layout)
        val expandView : LinearLayout = view.findViewById(R.id.expand)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item,parent,false)//inflate actual item view
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item : StudentDetails = dataset[position]
        holder.nameTV.text = item.fullName
//        holder.emailTV.text = item.collegeEmail
        holder.pointsTV.text = item.points.toString()
        holder.id.text = item.idNumber
        holder.rank.text = (position+1).toString()

        val isVisible : Boolean = item.visibility
        holder.expand.visibility = if(isVisible) View.VISIBLE else View.GONE

        holder.expandView.setOnClickListener{
            item.visibility = !item.visibility
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}