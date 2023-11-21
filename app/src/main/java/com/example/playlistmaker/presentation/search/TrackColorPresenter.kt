package com.example.playlistmaker.presentation.search

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.domain.SearchTracksViewColor

class TrackColorPresenter (val activityFun: SearchTracksViewColor
) : AppCompatActivity(){

    fun onCreate(){
        properColors()
    }
    private fun properColors(){
        var color:String
        if (isDarkModeOn()) {
            color="#1A1B22"
        } else {
            color="#AEAFB4"
        }
        activityFun.changeHint(color)//не получалось поменять цвет в атрибутах, поменял тут
    }

    private fun isDarkModeOn(): Boolean {
        val currentNightMode = activityFun.getConfiguration()
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }
}