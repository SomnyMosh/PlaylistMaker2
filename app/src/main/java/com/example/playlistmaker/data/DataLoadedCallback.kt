package com.example.playlistmaker.data

import com.example.playlistmaker.domain.models.Track

interface DataLoadedCallback {
    fun onDataLoaded(tracks: ArrayList<Track>)
    fun onError(code: Int)
}