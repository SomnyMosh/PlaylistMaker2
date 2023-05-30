package com.example.playlistmaker

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class SearchActivity : AppCompatActivity() {

    var editedText:String=""
    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT,editedText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        var searchInput=savedInstanceState.getString(SEARCH_TEXT,"")
        editedText = searchInput

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_search)
        val getBack = findViewById<TextView>(R.id.searchArrowBackButton)
        var searchView = findViewById<SearchView>(R.id.search_view)
        searchView.setQuery(editedText, false)


        val id: Int = searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
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
            override fun onQueryTextSubmit(p0: String?): Boolean { // знаю, что searchview уже сохраняет данные, просто показываю, что я понял тему (я сначало подумал, что searchview — это кастомный edittext)
                if(p0!=null){
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
    }
    private fun isDarkModeOn(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

}