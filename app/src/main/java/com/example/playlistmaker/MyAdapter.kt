package com.example.playlistmaker

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.Locale

class MyAdapter(private val trackList : ArrayList<Track>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(trackList[position])

    }
    class MyViewHolder( itemView : View) : RecyclerView.ViewHolder(itemView){
        val titleImage : ShapeableImageView = itemView.findViewById(R.id.track_cover)
        val heading : TextView = itemView.findViewById(R.id.track_title)
        val undertext : TextView = itemView.findViewById(R.id.track_subtext)
        fun bind (model : Track){
            val currentItem = model
            Glide.with(itemView)
                .load(currentItem.artworkUrl100)
                .apply(RequestOptions()
                    .placeholder(R.drawable.a)
                )
                .into(titleImage)
            heading.text = currentItem.trackName
            undertext.text = "${currentItem.artistName} ● ${SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentItem.trackTimeMillis.toLong())}"
        }
    }
}