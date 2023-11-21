package com.example.playlistmaker.domain.models

import java.text.SimpleDateFormat
import java.util.Locale

class Track (var trackName: String, var artistName: String, var trackTimeMillis: Int, var artworkUrl100: String, var primaryGenreName: String, var collectionName: String?, var releaseDate: String, var country: String, var previewUrl:String) {
}
