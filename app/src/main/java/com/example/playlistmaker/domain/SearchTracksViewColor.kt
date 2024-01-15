package com.example.playlistmaker.domain

interface SearchTracksViewColor {
    fun getConfiguration():Int
    fun changeHint(color:String)
}