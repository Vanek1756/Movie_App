package com.example.movie.view

import android.os.Bundle
import android.widget.Toast
import com.example.movie.databinding.YoutubeActivityBinding
import com.example.movie.model.result.ResultTrailerMovie
import com.example.movie.util.API_KEY_YOUTUBE
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer

class YouTubeActivity : YouTubeBaseActivity() {
    companion object {
        const val YOUTUBE_EXTRA = "youTubeData"
    }

    private lateinit var resultTrailer: ResultTrailerMovie

    private lateinit var binding: YoutubeActivityBinding

    private lateinit var youtubePlayerInit: YouTubePlayer.OnInitializedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = YoutubeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        if (intent != null && intent.hasExtra(YOUTUBE_EXTRA)) {
            resultTrailer = intent.getParcelableExtra(YOUTUBE_EXTRA)!!
            with(binding) {
                youtubePlayerInit = object : YouTubePlayer.OnInitializedListener {
                    override fun onInitializationSuccess(
                        p0: YouTubePlayer.Provider?,
                        p1: YouTubePlayer?,
                        p2: Boolean
                    ) {
                        p1?.loadVideo(resultTrailer.key)
                    }

                    override fun onInitializationFailure(
                        p0: YouTubePlayer.Provider?,
                        p1: YouTubeInitializationResult?
                    ) {
                        Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
                youTubePlayerView.initialize(API_KEY_YOUTUBE, youtubePlayerInit)
            }
        }
    }
}
