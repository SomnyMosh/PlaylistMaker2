package com.example.playlistmaker.presentation.track

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.data.OnItemClickListener
import com.example.playlistmaker.databinding.ItemListBinding
import com.example.playlistmaker.domain.models.Track
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.Locale
class MyViewHolder(private val binding:ItemListBinding) : RecyclerView.ViewHolder(binding.root){

    fun bind(model: Track) {
        Log.d("MyViewHolder", "Binding track: ${model.trackName}")
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.a)
            )
            .into(binding.trackCover)
        binding.trackTitle.text = model.trackName
        binding.trackSubtext.text ="${model.artistName} ● ${
            SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(model.trackTimeMillis.toLong())
        }"
    }
}