package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.DTOResponse
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.ResponseType
import com.example.playlistmaker.domain.models.Track

class TrackRepositoryImpl():TrackRepository {
    private val retrofitNetworkClient : RetrofitNetworkClient = RetrofitNetworkClient()
    lateinit var newArrayList : ArrayList<Track>
    lateinit var response : DTOResponse
    override fun searchTracks(expression: String): ArrayList<Track> {
        newArrayList = arrayListOf<Track>()
        newArrayList = retrofitNetworkClient.searchTracks(expression)
        response=retrofitNetworkClient.doRequest()
        if (retrofitNetworkClient.responseType == ResponseType.PROPER_RESPONSE){
            return newArrayList
        }
        newArrayList.clear()
        return newArrayList
    }
}