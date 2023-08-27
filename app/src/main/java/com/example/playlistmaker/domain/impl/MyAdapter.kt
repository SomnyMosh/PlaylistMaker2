package com.example.playlistmaker.domain.impl

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.Locale

@Suppress("DEPRECATION")
class MyAdapter(
    private val trackList: ArrayList<Track>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(trackList[position])

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnClickListener {
        private val titleImage: ShapeableImageView = itemView.findViewById(R.id.track_cover)
        private val heading: TextView = itemView.findViewById(R.id.track_title)
        private val undertext: TextView = itemView.findViewById(R.id.track_subtext)

        fun bind(model: Track) {
            Glide.with(itemView)
                .load(model.artworkUrl100)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.a)
                )
                .into(titleImage)
            heading.text = model.trackName
            undertext.text = "${model.artistName} ● ${
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(model.trackTimeMillis.toLong())
            }"
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}