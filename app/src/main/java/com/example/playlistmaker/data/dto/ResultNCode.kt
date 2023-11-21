package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.models.Track

class ResultNCode (var code:Int, var results: List<Track>):Response(){
}