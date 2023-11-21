package com.example.playlistmaker.di.search_module

import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.history.HistoryImpl
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.history.History
import com.example.playlistmaker.domain.impl.TrackInteractorImpl
import org.koin.dsl.module

val repositoryTrackModule = module {
    single <TrackRepository> {
        TrackRepositoryImpl(get())
    }
    single <History> {
        HistoryImpl(get(), get())
    }

}