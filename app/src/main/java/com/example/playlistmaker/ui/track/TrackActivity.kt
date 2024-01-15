package com.example.playlistmaker.ui.track

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.SaveData
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackActivity : AppCompatActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIME_REFRESH = 400L
    }
    private var mainThreadHandler: Handler? = null
    private var playerState = STATE_DEFAULT
    private lateinit var saveData: SaveData
    private var mediaPlayer = MediaPlayer()
    private lateinit var play: ImageView
    private var timeMonitorTextView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)
        supportActionBar?.hide()
        setContentView(R.layout.activity_track)
        val track: Track
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
        play=findViewById(R.id.play_button)
        val getBack = findViewById<ShapeableImageView>(R.id.track_arrow_back_button)
        timeMonitorTextView = findViewById(R.id.track_time)
        mainThreadHandler = Handler(Looper.getMainLooper())
        Glide.with(applicationContext)
            .load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
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
        year.text= track.releaseDate!!.take(4)
        genre.text=track.primaryGenreName
        country.text=track.country
        preparePlayer(track.previewUrl, play)
        getBack.setOnClickListener {
            finish()
        }
        play.setOnClickListener {
            playbackControl(play)
            startTimer()
        }
    }
    override fun onPause() {
        super.onPause()
        pausePlayer(play)
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun preparePlayer(url: String, playButton:ImageView) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.play_icon))
            playerState = STATE_PREPARED
        }
    }
    private fun startPlayer(playButton:ImageView) {
        mediaPlayer.start()
        playButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pause_icon))
        playerState = STATE_PLAYING
    }

    private fun pausePlayer(playButton:ImageView) {
        mediaPlayer.pause()
        playButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.play_icon))
        playerState = STATE_PAUSED
    }
    private fun playbackControl(playButton:ImageView) {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer(playButton)
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer(playButton)
            }
        }
    }
    private fun startTimer() {
        mainThreadHandler?.post(
            createUpdateTimerTask()
        )
    }
    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if(playerState== STATE_PLAYING){
                    timeMonitorTextView?.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    mainThreadHandler?.postDelayed(this, TIME_REFRESH)
                }
            }
        }
    }
}