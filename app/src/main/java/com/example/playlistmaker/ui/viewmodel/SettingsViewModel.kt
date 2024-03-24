package com.example.playlistmaker.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.MyApplication
import com.example.playlistmaker.domain.SettingsInteractor
import com.example.playlistmaker.domain.SettingsViewModelActions

class SettingsViewModel(private var settingsInteractor: SettingsInteractor) : ViewModel() {
    // LiveData or StateFlow to hold the dark mode state

    private var actions: SettingsViewModelActions? = null
    fun setActions(actions: SettingsViewModelActions){
        this.actions = actions
    }
    fun changeTheme(dark:Boolean){
        settingsInteractor.changeTheme(dark)
    }
    fun isDarkModeOn(): Boolean{
        return settingsInteractor.isDark()
    }
    fun shareApp() {
        val url = MyApplication.getAppContext()?.resources?.getString(R.string.settings_link_share)
        actions?.shareApp(url ?: "")
    }

    fun openTerms() {
        val url = MyApplication.getAppContext()?.resources?.getString(R.string.settings_link)
        actions?.openTerms(url ?: "")
    }

    fun contactSupport() {
        val message = MyApplication.getAppContext()?.resources?.getString(R.string.settings_message)
        val title = MyApplication.getAppContext()?.resources?.getString(R.string.settings_title)
        val email = MyApplication.getAppContext()?.resources?.getString(R.string.settings_email)
        actions?.contactSupport(message?:"", title?:"", email?:"")
    }


}