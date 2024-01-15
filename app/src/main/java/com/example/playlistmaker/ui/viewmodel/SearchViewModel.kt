package com.example.playlistmaker.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.playlistmaker.domain.api.TrackInteractor
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.playlistmaker.domain.history.HistoryInteractor
import com.example.playlistmaker.domain.models.ErrorType
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.viewmodel.states.StatesOfSearching
import org.koin.java.KoinJavaComponent.inject


class SearchViewModel(private var searchInteractor: TrackInteractor, private var historyInteractor: HistoryInteractor) : ViewModel() {

    private var results: MutableLiveData<List<Track>?> = MutableLiveData<List<Track>?>()

    private var searchLiveData =
        MutableLiveData<StatesOfSearching>(StatesOfSearching.Search)

    private var searchHistoryList: MutableLiveData<List<Track>> =
        MutableLiveData<List<Track>>().apply {
            value = emptyList()
        }

    fun getSearchLiveData(): LiveData<StatesOfSearching> {
        return searchLiveData
    }

    private val searchingTrackConsumer = object : TrackInteractor.TracksConsumer {
        override fun consume(tracks: List<Track>?, errorMessage: ErrorType?) {
            when (errorMessage) {
                ErrorType.CONNECTION_ERROR -> searchLiveData.postValue(StatesOfSearching.ErrorConnection)
                ErrorType.SERVER_ERROR -> searchLiveData.postValue(StatesOfSearching.ErrorFound)

                else -> {
                    results.postValue(tracks)
                    searchLiveData.postValue(
                        if (tracks.isNullOrEmpty())
                            StatesOfSearching.ErrorFound
                        else StatesOfSearching.SearchCompleted(tracks)
                    )
                }
            }
        }
    }

    fun requestSearch(expression: String) {
        searchLiveData.postValue(StatesOfSearching.Loading)
        searchInteractor.searchTracks(expression, searchingTrackConsumer)
    }

    fun add(it: Track) {
        historyInteractor.add(it)
    }

    fun clearHistory() {
        historyInteractor.clearHistory()
    }

    fun provideSearchHistory(): LiveData<List<Track>> {
        val history = historyInteractor.provideSearchHistory()
        searchHistoryList.value = historyInteractor.provideSearchHistory()
        if (history.isNullOrEmpty()) {
            searchHistoryList.postValue(emptyList())
        }
        return searchHistoryList
    }
}