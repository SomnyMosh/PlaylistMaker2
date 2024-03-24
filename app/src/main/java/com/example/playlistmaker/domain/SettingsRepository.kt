package com.example.playlistmaker.domain

interface SettingsRepository {
    fun changeTheme(isChecked: Boolean)

    fun isDark(): Boolean
}