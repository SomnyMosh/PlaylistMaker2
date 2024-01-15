package com.example.playlistmaker.domain.impl


import com.example.playlistmaker.data.dto.Resource
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import java.util.concurrent.Executors

class TrackInteractorImpl (private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()


    override fun searchTracks(expression: String, consumer: TrackInteractor.TracksConsumer) {
        executor.execute {
            when (val tracksData=repository.searchTracks(expression)) {
                is Resource.Success -> { consumer.consume(tracksData.data, null) }
                is Resource.Error -> { consumer.consume(null, tracksData.message) }
            }
        }
    }

}
