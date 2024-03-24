package com.example.playlistmaker.domain

interface SettingsViewModelActions {
    fun shareApp(url: String)
    fun openTerms(url: String)
    fun contactSupport(message:String, title:String, email:String)
}