package com.example.playlistmaker.data.network

import com.example.playlistmaker.domain.api.ITunesService
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.DTOResponse
import com.example.playlistmaker.data.dto.TracksSearchRequest
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitNetworkClient : NetworkClient {
    val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
    private var iTunesService = retrofit.create(ITunesService::class.java)
    override fun doRequest(dto:Any): DTOResponse {
        if (dto is TracksSearchRequest) {
            val resp = iTunesService.search(dto.expression).execute()
            val dtoResponse: DTOResponse= DTOResponse()
            val body = resp.body() ?: DTOResponse()
            return body.apply { resultCode = resp.code() }
        } else {
            return DTOResponse().apply { resultCode = 400 }
        }
    }
}