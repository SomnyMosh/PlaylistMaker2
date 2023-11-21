package com.example.playlistmaker.domain.history

import com.example.playlistmaker.domain.models.Track

class HistoryInteractorImpl(private val repository: History): HistoryInteractor {

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun add(it: Track) {
        repository.add(it)
    }

    override fun provideSearchHistory(): List<Track> {
        return repository.provideSearchHistory()
    }
}