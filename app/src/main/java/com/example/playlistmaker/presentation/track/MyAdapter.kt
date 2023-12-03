package com.example.playlistmaker.presentation.track

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.OnItemClickListener
import com.example.playlistmaker.databinding.ItemListBinding
import com.example.playlistmaker.domain.models.Track

@Suppress("DEPRECATION")
class MyAdapter(val listener: Click) : RecyclerView.Adapter<MyViewHolder>() {
    private var _it: List<Track> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        Log.d("MyAdapter", "y e s ${viewType}")
        val view =
            LayoutInflater.from(parent.context)
        return MyViewHolder(ItemListBinding.inflate(view, parent, false))
    }

    override fun getItemCount(): Int {
        Log.d("MyAdapter", "Item count: ${_it.size}")
        return _it.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(_it[position])
        holder.itemView.setOnClickListener {
            listener.onClick(_it[position])
            notifyDataSetChanged()
        }
        Log.d("MyAdapter", "y e s")
    }
    fun interface Click {
        fun onClick(track: Track)
    }
    fun setIt(it: List<Track>) {
        Log.d("MyAdapter", "Setting data of size: ${it.size}")
        _it = it
        notifyDataSetChanged()
    }
}