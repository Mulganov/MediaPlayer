package com.android.mediaplayer

import android.app.Activity
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Filter
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import com.android.mediaplayer.filter.HueFilter
import com.android.mediaplayer.filter.NoEffectFilter
import com.android.mediaplayer.interfaces.Controller
import com.android.mediaplayer.model.AssetsMetadataExtractor
import com.android.mediaplayer.model.Metadata
import com.videffects.sample.interfaces.ProgressChangeListener
import com.videffects.sample.model.transformHue
import java.io.File

class VideoController(private var activity: Activity?, val controller: Controller,
                      filename: String) {

    companion object {
        private const val TAG = "kVideoController"
    }

    private var filter: NoEffectFilter = NoEffectFilter()

    private var assetFileDescriptor: AssetFileDescriptor = activity?.assets?.openFd(filename)
            ?: throw RuntimeException("Asset not found")

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var metadata: Metadata? = AssetsMetadataExtractor().extract(assetFileDescriptor)

    private val intensityChangeListener: ProgressChangeListener = object : ProgressChangeListener() {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            filter.run {
                when (this) {
                    is HueFilter -> setIntensity(transformHue(progress))
                }
            }
        }
    }

    init {
        setupMediaPlayer()
        setupView()
    }

    private fun setupMediaPlayer() {
        mediaPlayer.isLooping = true
        mediaPlayer.setDataSource(
                assetFileDescriptor.fileDescriptor,
                assetFileDescriptor.startOffset,
                assetFileDescriptor.length
        )

        // https://developer.android.com/reference/android/media/MediaPlayer.OnErrorListener
        mediaPlayer.setOnErrorListener { _, what, extra ->
            Log.d(TAG, "OnError! What: $what; Extra: $extra")
            false
        }

        mediaPlayer.setOnCompletionListener {
            Log.d(TAG, "OnComplete!")
        }
    }

    private fun setupView() {
        val metadata = this.metadata
        val activity = this.activity
        if (metadata != null && activity != null) {
            controller.setupVideoSurfaceView(mediaPlayer, metadata.width, metadata.height)
        }
    }

    fun onPause() {
        mediaPlayer.pause()
    }

    fun onDestroy() {
        mediaPlayer.stop()
        mediaPlayer.release()
        assetFileDescriptor.close()
        activity = null
    }
}