package com.example.playlistmaker.data.history

import android.content.SharedPreferences
import com.example.playlistmaker.domain.history.History
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryImpl (private val savedSearchHistory: SharedPreferences,
private val gson: Gson) : History {

    companion object {
        const val SEARCH_SHARED_PREFERENCE = "search"
        const val number_10 = 10
        const val number_9 = 9
        const val number_0 = 0
    }

    private var trackHistoryList = ArrayList<Track>()

    override fun clearHistory() {
        trackHistoryList.clear()
        save()
    }

    override fun add(newIt: Track) {
        val json = savedSearchHistory.getString(SEARCH_SHARED_PREFERENCE, "")
        if (json != null && json.isNotEmpty() && trackHistoryList.isEmpty() && savedSearchHistory.contains(
                SEARCH_SHARED_PREFERENCE
            )
        ) {
            val type = object : TypeToken<ArrayList<Track>>() {}.type
            trackHistoryList = gson.fromJson(json, type)
        }
        if (trackHistoryList.contains(newIt)) {
            trackHistoryList.remove(newIt)
            trackHistoryList.add(number_0, newIt)
        } else {
            if (trackHistoryList.size < number_10)
                trackHistoryList.add(number_0, newIt)
            else {
                trackHistoryList.removeAt(number_9)
                trackHistoryList.add(number_0, newIt)
            }
        }
        save()
    }

    override fun provideSearchHistory(): List<Track> {
        val json = savedSearchHistory.getString(SEARCH_SHARED_PREFERENCE, "")
        if (json != null) {
            if (json.isNotEmpty()) {
                if (trackHistoryList.isEmpty()) {
                    if (savedSearchHistory.contains(SEARCH_SHARED_PREFERENCE)) {
                        val type = object : TypeToken<ArrayList<Track>>() {}.type
                        trackHistoryList = gson.fromJson(json, type)
                    }
                }
            } else
                trackHistoryList = ArrayList()
        }
        return trackHistoryList
    }

    private fun save() {
        var json = ""
        json = gson.toJson(trackHistoryList)
        savedSearchHistory.edit()
            .clear()
            .putString(SEARCH_SHARED_PREFERENCE, json)
            .apply()
    }
}