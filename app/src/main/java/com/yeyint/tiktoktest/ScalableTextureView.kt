package com.yeyint.tiktoktest

import android.content.Context
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.view.Surface
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener


class ScalableTextureView : TextureView, SurfaceTextureListener {
    private val mTransformMatrix = Matrix()

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        init()
    }

    private fun init() {
        surfaceTextureListener = this
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        // Initialize the media player with the new Surface
        val s = Surface(surface)
        // Set the scaling mode here using the Matrix
        // For example, to fit the video within the view:
        mTransformMatrix.setScale(width.toFloat() / width, height.toFloat() / height)
        setTransform(mTransformMatrix)
        // Set up your media player and start playback
        // mediaPlayer.setSurface(s);
        // mediaPlayer.start();
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        // Handle size change, if necessary
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        // Release resources and stop playback
        // mediaPlayer.release();
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
        // Handle updates, if necessary
    }
}
