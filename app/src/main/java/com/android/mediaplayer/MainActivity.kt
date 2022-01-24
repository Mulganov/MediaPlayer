package com.android.mediaplayer

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaPlayer
import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.mediaplayer.effect.TintEffect
import com.android.mediaplayer.filter.NoEffectFilter
import com.android.mediaplayer.interfaces.Controller
import com.android.mediaplayer.interfaces.Filter
import com.android.mediaplayer.interfaces.ShaderInterface
import com.videffects.sample.model.resizeView


class MainActivity : AppCompatActivity() , Controller{

    lateinit var videoSurfaceView: VideoSurfaceView
    lateinit var videoController: VideoController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        videoSurfaceView = findViewById(R.id.videoView)

        videoController = VideoController(this, this, "video.mp4")
    }

    override fun setupVideoSurfaceView(mediaPlayer: MediaPlayer, width: Double, height: Double) {
        videoSurfaceView.resizeView(width, height)
        videoSurfaceView.init(mediaPlayer, NoEffectFilter())

        onSelectShader( TintEffect(Color.GREEN) )
    }


    override fun onResume() {
        super.onResume()
        videoSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        videoController?.onPause()
        videoSurfaceView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoController?.onDestroy()
    }

    fun onSelectShader(shader: ShaderInterface) {
        videoSurfaceView.setShader(shader)
    }

    fun onSelectFilter(filter: Filter) {
        videoSurfaceView.setFilter(filter)
    }
}