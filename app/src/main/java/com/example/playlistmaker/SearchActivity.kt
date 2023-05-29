package com.example.playlistmaker

import android.R
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.security.AccessController.getContext


class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_search)
        val getBack = findViewById<TextView>(R.id.searchArrowBackButton)
        val searchView = findViewById<SearchView>(R.id.search_view)
        val id: Int = searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
        searchView.findViewById<TextView>(id).setTextColor(Color.parseColor("#AEAFB4"))
        getBack.setOnClickListener {
            finish()
        }
    }
    private fun isDarkModeOn(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }
}