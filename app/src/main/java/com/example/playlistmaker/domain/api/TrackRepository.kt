package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.dto.Resource
import com.example.playlistmaker.domain.models.Track

interface TrackRepository {

    fun searchTracks (expression: String): Resource<List<Track>>
}