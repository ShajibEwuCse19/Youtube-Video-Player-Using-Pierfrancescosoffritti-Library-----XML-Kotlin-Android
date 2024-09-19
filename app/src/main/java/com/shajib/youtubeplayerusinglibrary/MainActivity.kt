package com.shajib.youtubeplayerusinglibrary

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions

class MainActivity : AppCompatActivity() {

    private lateinit var youTubePlayer: YouTubePlayer
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var fullscreenViewContainer: FrameLayout
    private lateinit var enterFullscreenButton: Button
    private lateinit var playButton: Button
    private lateinit var pauseButton: Button
    private var fullScreen = false
    private val videoId = "ba9nkw_zUrc"

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (fullScreen) {
                youTubePlayer.toggleFullscreen()
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        youTubePlayerView = findViewById(R.id.youtube_player_view)
        fullscreenViewContainer = findViewById(R.id.full_screen_view_container)
        enterFullscreenButton = findViewById(R.id.btn_fullscreen)
        playButton = findViewById(R.id.btn_play)
        pauseButton = findViewById(R.id.btn_pause)

        lifecycle.addObserver(youTubePlayerView)
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        setupPlayerControls()
        initializeYouTubePlayer()
        setupFullScreenListener()
    }

    private fun setupPlayerControls() {
        playButton.setOnClickListener {
            youTubePlayer.play()
        }
        pauseButton.setOnClickListener {
            youTubePlayer.pause()
        }
        enterFullscreenButton.setOnClickListener {
            youTubePlayer.toggleFullscreen()
        }
    }

    private fun initializeYouTubePlayer() {
        val listener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                this@MainActivity.youTubePlayer = youTubePlayer
                youTubePlayer.cueVideo(videoId, 0f)

                // Set up custom UI interactions (directly from the layout)
                playButton.setOnClickListener {
                    youTubePlayer.play()
                }
                pauseButton.setOnClickListener {
                    youTubePlayer.pause()
                }
                enterFullscreenButton.setOnClickListener {
                    youTubePlayer.toggleFullscreen()
                }
            }
        }

        // Disable iframe UI controls
        val options = IFramePlayerOptions.Builder().controls(0).build()
        youTubePlayerView.initialize(listener, options)
    }

    private fun setupFullScreenListener() {
        youTubePlayerView.addFullscreenListener(object : FullscreenListener {
            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                toggleFullScreenView(fullscreenView, true)
            }

            override fun onExitFullscreen() {
                toggleFullScreenView(null, false)
            }
        })
    }

    private fun toggleFullScreenView(fullscreenView: View?, isFullscreen: Boolean) {
        fullScreen = isFullscreen

        if (isFullscreen) {
            youTubePlayerView.visibility = View.GONE
            fullscreenViewContainer.visibility = View.VISIBLE
            fullscreenView?.let { fullscreenViewContainer.addView(it) }

            // Hide controls in fullscreen
            playButton.visibility = View.GONE
            pauseButton.visibility = View.GONE
            enterFullscreenButton.visibility = View.GONE
        } else {
            youTubePlayerView.visibility = View.VISIBLE
            fullscreenViewContainer.visibility = View.GONE
            fullscreenViewContainer.removeAllViews()

            // Show controls when exiting fullscreen
            playButton.visibility = View.VISIBLE
            pauseButton.visibility = View.VISIBLE
            enterFullscreenButton.visibility = View.VISIBLE
        }
    }
}
