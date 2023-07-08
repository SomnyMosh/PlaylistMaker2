package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences

class SaveData (context: Context) {
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("file", Context.MODE_PRIVATE)
    private var sharedPreferencesTrack: SharedPreferences=context.getSharedPreferences("list", Context.MODE_PRIVATE)

    fun setDarkModeState (state: Boolean?){
        val editor = sharedPreferences.edit()
        editor.putBoolean("Dark", state!!)
        editor.apply()
    }

    fun loadDarkModeState(): Boolean?{
        val state= sharedPreferences.getBoolean("Dark", true)
        return (state)
    }
    fun setTracks(track: String?){
        val editor = sharedPreferencesTrack.edit()
        editor.putString("Tracks", track)
        editor.apply()
    }
    fun loadTracks(): String?{
        val state = sharedPreferences.getString("Tracks", null)
        return (state)
    }
}