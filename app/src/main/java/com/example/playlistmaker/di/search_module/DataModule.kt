package com.example.playlistmaker.di.search_module

import android.content.Context
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.history.HistoryImpl.Companion.SEARCH_SHARED_PREFERENCE
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.ITunesService
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<ITunesService> {

        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesService::class.java)

    }

    single {
        androidContext()
            .getSharedPreferences(SEARCH_SHARED_PREFERENCE, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
    }
}