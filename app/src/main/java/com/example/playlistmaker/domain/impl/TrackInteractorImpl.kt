package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.DataLoadedCallback
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import java.util.concurrent.Executors

class TrackInteractorImpl (private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, callback: DataLoadedCallback) {
        executor.execute {
            val tracks = repository.searchTracks(expression)
            val code = repository.whatever
            callback.onDataLoaded(tracks)
            callback.onError(code)
        }
    }

}
