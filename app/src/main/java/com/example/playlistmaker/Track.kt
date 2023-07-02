package com.example.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale

data class Track (var trackName: String, var artistName: String, var trackTimeMillis: Int, var artworkUrl100: String){
    private val trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis.toLong())
    val subtext = "$artistName ● $trackTime"

}