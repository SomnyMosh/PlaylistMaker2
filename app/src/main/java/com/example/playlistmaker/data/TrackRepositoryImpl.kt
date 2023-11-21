package com.example.playlistmaker.data

import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.data.dto.DataTrack
import com.example.playlistmaker.data.dto.Resource
import com.example.playlistmaker.data.dto.ResultNCode
import com.example.playlistmaker.data.dto.SearchRequest
import com.example.playlistmaker.domain.models.ErrorType
import com.example.playlistmaker.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient):TrackRepository {

    override fun searchTracks(expression: String): Resource<List<Track>> {
        try {
            val response = networkClient.doRequest(SearchRequest(expression))
            return when (response.resultCode) {
                -1 -> {
                    Resource.Error(ErrorType.CONNECTION_ERROR)
                }

                200 -> {
                    Resource.Success((response as ResultNCode).results.map {
                        Track(
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.primaryGenreName,
                            it.collectionName,
                            it.releaseDate,
                            it.country,
                            it.previewUrl ?: "defaultUrl"
                        )
                    })
                }

                else -> {
                    Resource.Error(ErrorType.SERVER_ERROR)
                }
            }
        } catch (error: Error) {
            throw Exception(error)
        }
    }
}