package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.data.dto.DataTrack
import com.example.playlistmaker.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient):TrackRepository {
    override var whatever : Int =0
    override fun searchTracks(expression: String): ArrayList<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        whatever = response.resultCode
        if (response.resultCode == 200) {
            return (response as DataTrack).results.map {
                Track(it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100, it.primaryGenreName, it.collectionName, it.releaseDate, it.country, it.previewUrl) } as ArrayList<Track>
        } else {
            return arrayListOf()
        }
    }
}