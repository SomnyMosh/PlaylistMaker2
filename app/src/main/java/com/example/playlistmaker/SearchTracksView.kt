package com.example.playlistmaker

import com.example.playlistmaker.ui.viewmodel.states.StatesOfSearching
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface SearchTracksView: MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun render(state:StatesOfSearching)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showToast(message: String)
}