package com.example.playlistmaker.domain

import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.Track

interface SearchTracksViewSearch {

    fun setQuery(editedText:String) {
    }
    fun showProgressBar() {
    }
    fun assignSearchResults(tracks:ArrayList<Track>){
    }
    fun showSearchResults(){
    }
    fun showInternetError() {
    }
    fun showResultsError(){
    }
    fun saveToHistory(tracks:ArrayList<Track>){
    }
    fun recyclerViewVisibility(view: RecyclerView): Boolean
    fun getRecyclerView(history:Boolean):RecyclerView
    fun updateRecyclerView(history: Boolean, tracks:ArrayList<Track>)
    fun showHistory()
    fun getSaveData(): SaveData
}