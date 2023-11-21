package com.example.playlistmaker.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.playlistmaker.domain.api.ITunesService
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.DTOResponse
import com.example.playlistmaker.data.dto.SearchRequest
import com.example.playlistmaker.data.dto.TracksSearchRequest
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitNetworkClient(private val iTunesServer: ITunesService, private val context: Context) :
    NetworkClient {

    override fun doRequest(dto: Any): DTOResponse {
        if (!isConnected()) {
            return DTOResponse().apply { resultCode = -1 }
        }
        return if (dto is SearchRequest) {
            val resp = try {
                iTunesServer.search(dto.expression).execute()
            } catch (ex: Exception) {
                null
            }
            val body = resp?.body() ?: DTOResponse()
            body.apply {
                if (resp != null) {
                    resultCode = resp.code()
                }
            }
        } else {
            DTOResponse().apply { resultCode = 400 }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}