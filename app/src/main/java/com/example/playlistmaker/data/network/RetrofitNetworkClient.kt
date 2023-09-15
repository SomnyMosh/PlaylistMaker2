package com.example.playlistmaker.data.network

import android.view.View
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.dto.DTOResponse
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.impl.MyAdapter
import com.example.playlistmaker.domain.models.DataTrack
import com.example.playlistmaker.domain.models.ResponseType
import com.example.playlistmaker.domain.models.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private var iTunesService = retrofit.create(com.example.playlistmaker.domain.api.ITunesService::class.java)

    var responseType = ResponseType.RESULT_ERROR
    lateinit var newArrayList: ArrayList<Track>
    private var responseDone : Boolean = false
    override fun doRequest(): DTOResponse {
        when (responseType){
            ResponseType.RESULT_ERROR -> return DTOResponse().apply { resultCode = 400 }
            ResponseType.PROPER_RESPONSE -> return DTOResponse().apply { resultCode = 0 }
            ResponseType.INTERNET_ERROR ->return DTOResponse().apply { resultCode = 502 }
        }
        return DTOResponse().apply { resultCode = 1010111 }
    }
    fun searchTracks(p0:String):ArrayList<Track>{
        newArrayList = arrayListOf<Track>()
        iTunesService.search(p0).enqueue(object : Callback<DataTrack> {
            override fun onResponse(
                call: Call<DataTrack>,
                response: Response<DataTrack>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.resultCount != 0) {
                        responseType = ResponseType.PROPER_RESPONSE
                        newArrayList.clear()
                        newArrayList.addAll(response.body()?.results!!)
                        responseDone=true
                    } else {
                        responseType = ResponseType.RESULT_ERROR
                        responseDone=true
                    }
                }
            }

            override fun onFailure(call: Call<DataTrack>, t: Throwable) {
                responseType = ResponseType.INTERNET_ERROR
                responseDone=true
            }
        })
        while (!responseDone){

        }
        responseDone=false
        return newArrayList
    }
}