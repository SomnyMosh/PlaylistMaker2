package com.example.playlistmaker.domain.history

import android.app.Application
import com.example.playlistmaker.domain.MyApplication
import com.example.playlistmaker.domain.SaveData
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryInteractorImpl(): HistoryInteractor {

    var saveData: SaveData = SaveData(MyApplication.getAppContext()!!)
    var removableTrackPosition: Int = 0

    override fun clearHistory() {
        val emptyList: List<Track> = emptyList<Track>()
        val gson = Gson()
        val json: String = gson.toJson(emptyList)
        saveData.setTracks(json)
    }

    override fun add(it: Track) {
        var savedTracks = provideSearchHistory()
        val mutableList = savedTracks.toMutableList()
        if (iterateOnTracks(savedTracks, it)){
            mutableList.remove(savedTracks.get(removableTrackPosition))
            mutableList.add(it)
        }else{
            if(savedTracks.size >=10){
                mutableList.remove(savedTracks.get(0))
                mutableList.add(it)
            }else{
                mutableList.add(it)
            }
        }
        savedTracks=mutableList.toList()
        val gson = Gson()
        val json: String = gson.toJson(savedTracks)
        saveData.setTracks(json)
    }

    override fun provideSearchHistory(): List<Track> {
        val gson = Gson()
        val type = object : TypeToken<List<Track>>() {}.type
        return if (saveData.loadTracks() != null) {
            reverse(gson.fromJson(saveData.loadTracks(), type))
        } else {
            val emptyList: List<Track> = emptyList<Track>()
            (emptyList)
        }
    }
    private fun reverse(list: List<Track>): List<Track> {
        val reversedSavedTracks = list.reversed()
        return reversedSavedTracks
    }
    private fun iterateOnTracks(list: List<Track>, track: Track): Boolean {
        for (i in 0 until list.size) {
            if ((track.trackName==list.get(i).trackName)&&(track.artistName == list.get(i).artistName)&&(track.artworkUrl100==list.get(i).artworkUrl100)){
                removableTrackPosition = i
                return true
            }
        }
        return false
    }
}