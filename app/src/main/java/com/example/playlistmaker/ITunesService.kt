package com.example.playlistmaker

import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesService {

    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<DataTrack>

}