package com.example.playlistmaker.domain

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.playlistmaker.di.library_module.mediatekaModule
import com.example.playlistmaker.di.search_module.dataModule
import com.example.playlistmaker.di.search_module.repositoryTrackModule
import com.example.playlistmaker.di.search_module.searchInteractorModule
import com.example.playlistmaker.di.search_module.viewModelSearchingModule
import com.example.playlistmaker.di.settings_module.shareModule
import com.example.playlistmaker.di.track_module.playerModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin


class MyApplication: Application(), KoinComponent {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null
        fun getAppContext(): Context? {
            return context
        }
    }


    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        startKoin {
            androidContext(this@MyApplication)
            modules(
                playerModule,
                dataModule,
                repositoryTrackModule,
                searchInteractorModule,
                viewModelSearchingModule,
                shareModule,
                mediatekaModule
            )
        }
    }

}