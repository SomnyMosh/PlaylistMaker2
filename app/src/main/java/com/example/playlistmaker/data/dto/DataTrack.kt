package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.models.Track

data class DataTrack(var resultCount: Int, var results: List<TrackDto>) : DTOResponse()