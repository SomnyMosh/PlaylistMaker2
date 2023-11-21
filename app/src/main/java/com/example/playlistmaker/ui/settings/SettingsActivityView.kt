package com.example.playlistmaker.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.R


class SettingsActivityView : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_settings)
        val sup = findViewById<TextView>(R.id.support)
        val share = findViewById<TextView>(R.id.share)
        val userA = findViewById<TextView>(R.id.userAgreement)
        val getBack = findViewById<TextView>(R.id.arrowBackButton)
        val switchM : Switch =  findViewById(R.id.switch1)
        switchM.isChecked = isDarkModeOn()
        switchM!!.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        getBack.setOnClickListener {
            finish()
        }
        sup.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:")
            val message = getResources().getString(R.string.settings_message)
            val title = getResources().getString(R.string.settings_title)
            val email = getResources().getString(R.string.settings_email)
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, title)
            emailIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(emailIntent)
        }
        share.setOnClickListener {
            val shareIntent= Intent()
            shareIntent.action=Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_TEXT,getResources().getString(R.string.settings_link_share))
            shareIntent.type="text/plain"
            startActivity(Intent.createChooser(shareIntent,"Share To:"))
        }
        userA.setOnClickListener {
            val url = Uri.parse(getResources().getString(R.string.settings_link))
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
    }
    private fun isDarkModeOn(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }
}