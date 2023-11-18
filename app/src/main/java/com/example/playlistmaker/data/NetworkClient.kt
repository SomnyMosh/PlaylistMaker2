package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.DTOResponse


interface NetworkClient {
    fun doRequest(dto:Any):DTOResponse
}