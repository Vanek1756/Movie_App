package com.example.movie.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.movie.data.Storage
import com.example.movie.databinding.ActivityProfileBinding
import com.example.movie.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    @Inject
    lateinit var storage: Storage

    private val binding by viewBinding(ActivityProfileBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.tvEmailProfile.text = storage.readPreferencesSession()
    }
}
