package com.example.playlistmaker.domain.history

import com.example.playlistmaker.domain.models.Track

interface HistoryInteractor {
    fun clearHistory()

    fun add(it:Track)

    fun provideSearchHistory():List<Track>
}