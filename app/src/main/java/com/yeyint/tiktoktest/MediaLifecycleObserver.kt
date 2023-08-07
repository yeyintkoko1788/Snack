package com.yeyint.tiktoktest

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.exoplayer.ExoPlayer


class MediaLifecycleObserver(
    private val mediaPlayer: ExoPlayer?,
    lifecycleOwner: LifecycleOwner
) :
    LifecycleEventObserver {
    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

//    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
//    fun onPause() {
//        if (mediaPlayer != null && mediaPlayer.isPlaying) {
//            mediaPlayer.pause() // or mediaPlayer.stop() based on your needs
//        }
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
//    fun onResume() {
//        if (mediaPlayer != null && !mediaPlayer.isPlaying) {
//            mediaPlayer.play() // Resume playback if it was paused
//        }
//    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        Log.d("TAG", "${event} ${Lifecycle.Event.ON_PAUSE}")
        if (event == Lifecycle.Event.ON_PAUSE){
            if (mediaPlayer != null && mediaPlayer.isPlaying) {
                Log.d("TAG", "Onpause triggered")
                mediaPlayer.pause() // or mediaPlayer.stop() based on your needs
            }
        }else if (event == Lifecycle.Event.ON_RESUME){
            if (mediaPlayer != null && !mediaPlayer.isPlaying) {
                Log.d("TAG", "Onresume triggered")
                mediaPlayer.play() // Resume playback if it was paused
            }
        }
    }
}
