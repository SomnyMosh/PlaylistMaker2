package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TrackRepository {
    abstract val whatever: Int

    fun searchTracks (expression: String):ArrayList<Track>
}