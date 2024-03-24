package com.example.playlistmaker.data

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.MyApplication
import com.example.playlistmaker.domain.SettingsRepository

class SettingsRepositoryImpl() : SettingsRepository {
    override fun changeTheme(isChecked: Boolean) {
        if(isChecked){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun isDark(): Boolean {
        val currentNightMode = (MyApplication.getAppContext()?.resources?.configuration?.uiMode)?.and(
            Configuration.UI_MODE_NIGHT_MASK
        )
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

}