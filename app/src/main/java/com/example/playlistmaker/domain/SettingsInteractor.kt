package com.example.playlistmaker.domain

interface SettingsInteractor {

    fun changeTheme(isChecked: Boolean)

    fun isDark(): Boolean

}