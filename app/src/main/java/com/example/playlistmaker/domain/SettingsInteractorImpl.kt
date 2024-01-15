package com.example.playlistmaker.domain

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class SettingsInteractorImpl(private val context: Context) : SettingsInteractor {

    var flagDark = true

    override fun changeTheme(){
        if (isDark()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun isDark(): Boolean {
        val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

}