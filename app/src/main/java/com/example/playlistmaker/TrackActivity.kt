package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.Locale

class TrackActivity : AppCompatActivity() {
    private lateinit var saveData: SaveData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)
        supportActionBar?.hide()
        setContentView(R.layout.activity_track)
        val track:Track
        saveData = SaveData(this)
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        val savedTracks: ArrayList<Track> = gson.fromJson(saveData.loadTracks(), type)
        track = savedTracks.last()
        val albumPic = findViewById<ShapeableImageView>(R.id.track_album_cover)
        val name: TextView = findViewById(R.id.trackname)
        val artist : TextView = findViewById(R.id.track_artist)
        val duration: TextView = findViewById(R.id.track_time)
        val duration2: TextView = findViewById(R.id.track_duration_mutable)
        val album: TextView = findViewById(R.id.track_album_mutable)
        val year: TextView = findViewById(R.id.track_year_mutable)
        val genre: TextView = findViewById(R.id.track_genre_mutable)
        val country: TextView = findViewById(R.id.track_country_mutable)
        val getBack = findViewById<ShapeableImageView>(R.id.track_arrow_back_button)
        Glide.with(applicationContext)
            .load(track.artworkUrl100)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.a)
            )
            .into(albumPic)
        name.text = track.trackName
        artist.text=track.artistName
        duration.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(track.trackTimeMillis.toLong())
        duration2.text=duration.text
        if (track.collectionName!=null && track.collectionName!=""){
            album.visibility = VISIBLE
            album.text=track.collectionName
        }else{
            album.visibility = GONE
        }
        year.text=track.releaseDate.take(4)
        genre.text=track.primaryGenreName
        country.text=track.country
        getBack.setOnClickListener {
            finish()
        }
    }
}