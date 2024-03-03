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
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.content.ContextCompat

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
    private var firstTime = true
    private var resultsOn = false





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
        binding.closeButton.visibility = GONE
        binding.closeButton.setOnClickListener {
            binding.searchEditText.setText("")
            binding.searchEditText.clearFocus()
            binding.closeButton.visibility = GONE
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.closeButton.visibility = GONE
                } else {
                    binding.closeButton.visibility = VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchRequest()
                true
            } else {
                false
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
            binding.trackHistory.visibility = GONE
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
    override fun onBackPressed() {
        if (binding.searchEditText.hasFocus()) {
            binding.searchEditText.clearFocus()
        } else {
            super.onBackPressed()
        }
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
        resultsOn = data.size != 0
    }

    private fun historyTrigger(){
        if (firstTime == true){
            onFocus(false)
            firstTime=false
        }
        binding.searchEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                onFocus(true)
            } else {
                onFocus(false)
            }
        }
    }

    private fun onFocus(focus:Boolean?) {
        if (focus == false) {
            if (binding.searchEditText.text.toString().isNullOrEmpty())
                tracksSearchViewModel.provideSearchHistory()
                    .observe(this) { searchHistoryList ->
                        if (searchHistoryList.isNotEmpty()) {
                            history(searchHistoryList)
                        } else {
                            binding.trackHistory.visibility = GONE
                        }
                    }
        } else {
            if (resultsOn==true){
                binding.trackHistory.visibility = GONE
            }
        }
    }
    private fun onTextChange(){

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
        outState.putString(QUERY, binding.searchEditText.text.toString())
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }

    private fun toggleSearchResultsAndHistoryVisibility() {
        if (binding.tracks.visibility == View.VISIBLE) {
            binding.tracks.visibility = View.GONE
            binding.trackHistory.visibility = View.VISIBLE
        } else {
            binding.tracks.visibility = View.VISIBLE
            binding.trackHistory.visibility = View.GONE
        }
    }

    private fun searchRequest(){
        tracksSearchViewModel.requestSearch(binding.searchEditText.text.toString())
    }


    private fun isDarkModeOn(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }


}