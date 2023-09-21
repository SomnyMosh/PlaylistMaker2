package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.DataLoadedCallback
import com.example.playlistmaker.data.dto.ResultNCode
import com.example.playlistmaker.domain.models.Track

interface TrackInteractor {
    fun searchTracks(expression: String, callback: DataLoadedCallback)
    interface TracksConsumer {
        fun consume(foundTracks: ArrayList<Track>)
        fun consumeCode(Code:Int)
        fun show(): ResultNCode
    }
}