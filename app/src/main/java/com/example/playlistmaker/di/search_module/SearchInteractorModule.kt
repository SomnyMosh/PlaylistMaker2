package com.example.playlistmaker.di.search_module

import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.history.HistoryInteractor
import com.example.playlistmaker.domain.history.HistoryInteractorImpl
import com.example.playlistmaker.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.models.Track
import org.koin.dsl.module

val searchInteractorModule = module {
    single <TrackInteractor> {
        TrackInteractorImpl(get())
    }

    single <HistoryInteractor> {
        HistoryInteractorImpl(get())
    }
}