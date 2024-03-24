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
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.SettingsInteractor
import com.example.playlistmaker.domain.SettingsViewModelActions
import com.example.playlistmaker.ui.viewmodel.SettingsViewModel


class SettingsActivityView : AppCompatActivity(), SettingsViewModelActions {
    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_settings)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        viewModel.setActions(this)
        binding.switch1.isChecked = viewModel.isDarkModeOn()
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            viewModel.changeTheme(isChecked)
        }
        binding.arrowBackButton.setOnClickListener {
            finish()
        }
        binding.support.setOnClickListener{
            viewModel.contactSupport()
        }
        binding.share.setOnClickListener{
            viewModel.shareApp()
        }
        binding.userAgreement.setOnClickListener {
            viewModel.openTerms()
        }
    }

    override fun shareApp(url: String) {
        val shareIntent= Intent()
        shareIntent.action=Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT,Uri.parse(url))
        shareIntent.type="text/plain"
    }

    override fun openTerms(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun contactSupport(message: String, title: String, email: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title)
        emailIntent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(emailIntent)
    }
}