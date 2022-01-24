package com.android.mediaplayer.model

import com.android.mediaplayer.effect.AutoFixEffect
import com.android.mediaplayer.filter.HueFilter

// TODO: Rewite to factory
class Shaders(width: Int, height: Int) {

    companion object {
        private const val SUFFIX = "Effect"
    }

    private val shaders = arrayOf(
            // Filters
            HueFilter(),

            // Effects
            AutoFixEffect(0.0F)
    )

    val count = shaders.size

    fun getShader(index: Int) = shaders[index]

    fun getShaderName(index: Int) = shaders[index]::class.java.simpleName.removeSuffix(SUFFIX)
}