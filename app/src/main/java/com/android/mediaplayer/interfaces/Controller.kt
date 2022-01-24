package com.android.mediaplayer.interfaces

import android.media.MediaPlayer

interface Controller {
    fun setupVideoSurfaceView(mediaPlayer: MediaPlayer, width: Double, height: Double)
}