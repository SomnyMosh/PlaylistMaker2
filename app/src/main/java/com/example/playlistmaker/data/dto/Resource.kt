package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.models.ErrorType
import com.example.playlistmaker.domain.models.Track

sealed class Resource<T>(val data: T? = null, val message: ErrorType? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: ErrorType, data: T? = null) : Resource<T>(data, message)
}