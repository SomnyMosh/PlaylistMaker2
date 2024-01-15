package com.example.playlistmaker.ui.viewmodel.states

import com.example.playlistmaker.domain.models.Track

sealed class StatesOfSearching {
    object Loading : StatesOfSearching()
    object Search : StatesOfSearching()
    object ErrorConnection : StatesOfSearching()
    object ErrorFound : StatesOfSearching()

    object FirstOpened : StatesOfSearching()
    data class SavedResults(var history: List<Track>) : StatesOfSearching()
    data class SearchCompleted(val data: List<Track>) : StatesOfSearching()
}