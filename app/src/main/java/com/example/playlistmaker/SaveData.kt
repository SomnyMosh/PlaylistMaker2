package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences

class SaveData (context: Context) {
    private var sharedPreferencesTrack: SharedPreferences=context.getSharedPreferences("list", Context.MODE_PRIVATE)


    fun setTracks(track: String?){
        val editor = sharedPreferencesTrack.edit()
        editor.putString("Tracks", track)
        editor.apply()
    }
    fun loadTracks(): String?{
        val state = sharedPreferencesTrack.getString("Tracks", null)
        return (state)
    }
}