package com.example.playlistmaker.ui.search

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.presentation.track.MyAdapter
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.track.TrackActivity
import com.example.playlistmaker.ui.viewmodel.SearchViewModel
import com.example.playlistmaker.ui.viewmodel.states.StatesOfSearching
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.content.Context
import android.view.View
import android.widget.EditText

class SearchActivityView : AppCompatActivity() {


    private lateinit var binding: ActivitySearchBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchHistoryRecyclerView: RecyclerView
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var tracksAdapter: MyAdapter
    private lateinit var searchHistoryAdapter: MyAdapter
    private val tracksSearchViewModel by viewModel<SearchViewModel>()
    private val searchRunnable = Runnable { searchRequest() }
    private var isKeyboardShowing = false





    companion object {
        const val QUERY = "QUERY"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val DEBOUNCE_DELAY_3000L = 3000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (isDarkModeOn()) {
            changeHint("#1A1B22") //не получалось поменять цвет в атрибутах, поменял тут
        } else {
            changeHint("#AEAFB4")
        }
        tracksSearchViewModel.getSearchLiveData().observe(this) { searchLiveData ->
            when (val states = searchLiveData) {
                is StatesOfSearching.Loading -> loading()
                is StatesOfSearching.Search -> baseSearch()
                is StatesOfSearching.ErrorConnection -> errorConnection()
                is StatesOfSearching.ErrorFound -> errorFound()
                is StatesOfSearching.SavedResults -> history(states.history)
                is StatesOfSearching.SearchCompleted -> searchCompleted(states.data)
                else -> initial()
            }
        }

        historyTrigger()
        onTextChange()

        tracksAdapter = MyAdapter {
            if (clickDebounce())
                clicker(it)
        }
        binding.savedTracks.layoutManager = LinearLayoutManager(this)
        recyclerView = binding.tracks
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = tracksAdapter

        searchHistoryAdapter = MyAdapter {
            if (clickDebounce()) {
                clicker(it)
            }
        }

        searchHistoryRecyclerView = binding.savedTracks
        searchHistoryRecyclerView.layoutManager = LinearLayoutManager(this)
        searchHistoryRecyclerView.adapter = searchHistoryAdapter

        binding.clearButton.setOnClickListener {
            tracksSearchViewModel.clearHistory()
            binding.resultsError.visibility = GONE
        }

        tracksSearchViewModel.provideSearchHistory().observe(this) { value ->
            value.ifEmpty { emptyList() }
        }
        binding.refreshButton.setOnClickListener {
            searchRequest()
        }
        binding.searchArrowBackButton.setOnClickListener {
            finish()
        }

    }

    private fun initial() {
        binding.progressBarLayout.visibility = GONE
        binding.resultsError.visibility = GONE
        binding.trackHistory.visibility = GONE
        binding.tracks.visibility= GONE
        binding.noInternet.visibility= GONE
    }

    private fun history(history: List<Track>) {
        showHistory()
        searchHistoryAdapter.setIt(history)
        searchHistoryAdapter.notifyDataSetChanged()
    }

    private fun searchCompleted(data: List<Track>){
        Log.d("SearchActivityView", "Data size: ${data.size}")
        tracksAdapter.setIt(data)
        showSearchResults()
    }

    private fun historyTrigger(){
        binding.searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                onFocus(true)
            } else {
                onFocus(false)
            }
        }
    }
    override fun onBackPressed() {
        if (binding.searchView.isFocused) {
            // The SearchView is focused, handle the back press here
            binding.searchView.clearFocus() // This will cause the SearchView to lose focus
            // Optionally, collapse the SearchView if it is expanded
            // Additional logic if needed
        } else {
            // If the SearchView is not focused, call the super method to handle default back press behavior
            super.onBackPressed()
        }
    }
    private fun onFocus(focus:Boolean?) {
        if (focus == false) {
            if (binding.searchView.query == null || binding.searchView.query == "")
                tracksSearchViewModel.provideSearchHistory()
                    .observe(this) { searchHistoryList ->
                        if (searchHistoryList.isNotEmpty()) {
                            showHistory()
                        } else {
                            binding.trackHistory.visibility = GONE
                        }
                    }
        } else {
            binding.trackHistory.visibility = GONE
        }
    }
    private fun onTextChange(){
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener,
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                showProgressBar()
                if (p0 != null) {
                    searchRequest()
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if(p0!=null && p0!=""){
                    showProgressBar()
                    Toast.makeText(applicationContext, "text changed", Toast.LENGTH_SHORT).show()
                    searchDebounce()
                }
                return false
            }
        })
    }

    private fun clicker(item: Track) {
        tracksSearchViewModel.add(item)
        val intent = Intent(this, TrackActivity::class.java)
        this.startActivity(intent)
    }

    private fun searchDebounce(){
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        Toast.makeText(applicationContext, "Debounced", Toast.LENGTH_SHORT).show()
    }

    private fun errorFound() {
        binding.progressBarLayout.visibility = GONE
        binding.resultsError.visibility = VISIBLE
        binding.trackHistory.visibility = GONE
        binding.tracks.visibility= GONE
        binding.noInternet.visibility= GONE
    }

    private fun errorConnection() {
        binding.progressBarLayout.visibility = GONE
        binding.resultsError.visibility = GONE
        binding.trackHistory.visibility = GONE
        binding.tracks.visibility= GONE
        binding.noInternet.visibility= VISIBLE
    }

    private fun baseSearch() {
        binding.resultsError.visibility = GONE
        binding.trackHistory.visibility = GONE
        binding.tracks.visibility= GONE
        binding.noInternet.visibility= GONE
        binding.progressBarLayout.visibility= GONE
    }

    private fun loading(){
        binding.progressBarLayout.visibility= VISIBLE
        binding.resultsError.visibility = GONE
        binding.trackHistory.visibility = GONE
        binding.tracks.visibility= GONE
        binding.noInternet.visibility= GONE
    }

    fun showProgressBar(){
        binding.progressBarLayout.visibility= VISIBLE
    }
    private fun showSearchResults() {
        binding.progressBarLayout.visibility = GONE
        binding.resultsError.visibility = GONE
        binding.trackHistory.visibility = GONE
        binding.tracks.visibility= VISIBLE
        Toast.makeText(applicationContext, "SearchResults", Toast.LENGTH_SHORT).show()
        binding.noInternet.visibility= GONE
    }

    private fun showHistory(){
        binding.progressBarLayout.visibility = GONE
        binding.resultsError.visibility = GONE
        binding.trackHistory.visibility = VISIBLE
        binding.tracks.visibility= GONE
        binding.noInternet.visibility= GONE
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(QUERY, binding.searchView.query.toString())
    }

    private fun searchRequest(){
        tracksSearchViewModel.requestSearch(binding.searchView.query.toString())
    }


    private fun isDarkModeOn(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    private fun changeHint(color: String) {
        val id=
            binding.searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
        binding.searchView.findViewById<TextView>(id).setTextColor(Color.parseColor(color))
        binding.searchView.queryHint = Html.fromHtml(
            "<font color = $color>" + resources.getString(
                R.string.button_search
            ) + "</font>"
        )
    }
}