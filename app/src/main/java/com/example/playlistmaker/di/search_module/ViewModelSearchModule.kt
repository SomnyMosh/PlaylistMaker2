package com.example.playlistmaker.di.search_module

import androidx.lifecycle.SavedStateHandle
import com.example.playlistmaker.ui.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelSearchingModule = module{
    viewModel { SearchViewModel(get(), get()) }
}