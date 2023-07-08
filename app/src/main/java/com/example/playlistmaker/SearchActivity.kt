package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.webkit.RenderProcessGoneDetail
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    lateinit var newRecyclerView : RecyclerView
    lateinit var newArrayList: ArrayList<Track>
    private var editedText:String=""

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT,editedText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val searchInput=savedInstanceState.getString(SEARCH_TEXT,"")
        editedText = searchInput

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_search)
        val refreshButton = findViewById<Button>(R.id.refresh_button)
        var submitEditText:String?=""
        val internetError = findViewById<LinearLayout>(R.id.no_internet)
        val resultsError = findViewById<LinearLayout>(R.id.results_error)
        val getBack = findViewById<TextView>(R.id.searchArrowBackButton)
        val searchView = findViewById<SearchView>(R.id.search_view)
        searchView.setQuery(editedText, false)
        val id: Int = searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val searchCloseButtonId = searchView.context.resources
            .getIdentifier("android:id/search_close_btn", null, null)
        val closeButton = searchView.findViewById<ImageView>(searchCloseButtonId)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val ITunesService = retrofit.create(ITunesService::class.java)
        internetError.visibility = GONE
        resultsError.visibility = GONE

        // Set on click listener
        newRecyclerView=findViewById(R.id.tracks)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)
        newArrayList= arrayListOf<Track>()
        newRecyclerView.visibility = GONE
        closeButton.setOnClickListener {
            // Manage this event.
            editedText=""
            searchView.setQuery(editedText, false)
            this.currentFocus?.let { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
        val et = searchView.findViewById<View>(
            searchView.context.resources
                .getIdentifier("android:id/search_src_text", null, null)
        ) as EditText
        et.filters = arrayOf<InputFilter>(LengthFilter(50)) //максимальная длина текста
        if (isDarkModeOn()){
            searchView.findViewById<TextView>(id).setTextColor(Color.parseColor("#1A1B22"))
            searchView.setQueryHint(Html.fromHtml("<font color = #1A1B22>" + getResources().getString(R.string.button_search) + "</font>")) //не получалось поменять цвет в атрибутах, поменял тут
        }else{
            searchView.findViewById<TextView>(id).setTextColor(Color.parseColor("#3d3d3d"))
            searchView.setQueryHint(Html.fromHtml("<font color = #AEAFB4>" + getResources().getString(R.string.button_search) + "</font>"))
        }
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener,
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                newRecyclerView.visibility = GONE
                internetError.visibility = GONE
                resultsError.visibility = GONE
                submitEditText=p0
                if (submitEditText==null){
                    submitEditText=""
                }
                if(p0!=null){
                    ITunesService.search(p0).enqueue(object : Callback<DataTrack> {
                        override fun onResponse(call: Call<DataTrack>, response: Response<DataTrack>) {
                            if (response.isSuccessful){
                                if (response.body()?.resultCount != 0){
                                    newRecyclerView.visibility = VISIBLE
                                    newArrayList.clear()
                                    newArrayList.addAll(response.body()?.results!!)
                                    newRecyclerView.adapter = MyAdapter(newArrayList)
                                }else{
                                    resultsError.visibility = VISIBLE
                                }
                            }
                        }
                        override fun onFailure(call: Call<DataTrack>, t: Throwable) {
                            internetError.visibility = VISIBLE
                        }
                    })
                    if(editedText!=p0){
                        editedText=p0
                    }
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if(p0!=null){
                    if(editedText!=p0){
                        editedText=p0
                    }
                }
                return false
            }
        })
        getBack.setOnClickListener {
            finish()
        }
        refreshButton.setOnClickListener {
            ITunesService.search(submitEditText!!).enqueue(object : Callback<DataTrack> {
                override fun onResponse(call: Call<DataTrack>, response: Response<DataTrack>) {
                    if (response.isSuccessful){
                        if (response.body()?.resultCount != 0){
                            newRecyclerView.visibility = VISIBLE
                            newArrayList.clear()
                            newArrayList.addAll(response.body()?.results!!)
                            newRecyclerView.adapter = MyAdapter(newArrayList)
                        }else{
                            resultsError.visibility = VISIBLE
                        }
                    }
                }
                override fun onFailure(call: Call<DataTrack>, t: Throwable) {
                    internetError.visibility = VISIBLE
                }
            })
        }
    }

    private fun isDarkModeOn(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

}