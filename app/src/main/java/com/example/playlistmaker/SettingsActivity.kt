package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate


class SettingsActivity : AppCompatActivity() {
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
        var sharedPreferences = getSharedPreferences("save", MODE_PRIVATE)
        switchM.setChecked(sharedPreferences.getBoolean("value",true))
        switchM.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("value", true)
                editor.apply()
                switchM.setChecked(true)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("value", false)
                editor.apply()
                switchM.setChecked(false)
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
}