package com.example.playlistmaker.domain.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.OnItemClickListener
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.track.MyViewHolder

@Suppress("DEPRECATION")
class MyAdapter(
    private val trackList: ArrayList<Track>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return MyViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(trackList[position])
    }
}