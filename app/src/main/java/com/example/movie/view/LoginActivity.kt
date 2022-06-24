package com.example.movie.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.movie.data.Storage
import com.example.movie.databinding.ActivityLoginBinding
import com.example.movie.model.User
import com.example.movie.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var storage: Storage

    @Inject
    lateinit var user: User

    private val binding by viewBinding(ActivityLoginBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (!storage.readPreferencesUserName().isNullOrEmpty()) {
            user.id = storage.readPreferencesId()?.toInt()
            user.name = storage.readPreferencesName()
            user.userName = storage.readPreferencesUserName()
            user.sessionId = storage.readPreferencesSession()
            user.imageProfile = storage.readPreferencesImageProfile()

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}
