package com.example.playlistmaker.presentation.track

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.data.OnItemClickListener
import com.example.playlistmaker.domain.models.Track
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.Locale
class MyViewHolder(itemView: View, private val listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
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