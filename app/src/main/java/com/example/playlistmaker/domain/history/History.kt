package com.example.playlistmaker.domain.history

import com.example.playlistmaker.domain.models.Track

interface History {
    fun clearHistory()

    fun add(newIt: Track)

    fun provideSearchHistory():List<Track>
}